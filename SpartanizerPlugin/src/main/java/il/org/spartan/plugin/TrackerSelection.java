package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

public class TrackerSelection extends Selection {
  ASTNode track;
  int length;

  public TrackerSelection(WrappedCompilationUnit compilationUnit, ITextSelection textSelection, String name) {
    super(asList(compilationUnit), textSelection, name);
  }

  public static TrackerSelection empty() {
    return new TrackerSelection(null, null, null);
  }

  public TrackerSelection track(final ASTNode ¢) {
    assert ¢ != null;
    assert ¢ instanceof MethodDeclaration || ¢ instanceof AbstractTypeDeclaration;
    track = ¢;
    length = ¢.getLength();
    return this;
  }

  public void update() {
    inner.get(0).dispose();
    final ASTNode newTrack = fix(track.getNodeType(),
        track.getLength() > length
            ? new NodeFinder(inner.get(0).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveringNode()
            : new NodeFinder(inner.get(0).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveredNode());
    if (!match(track, newTrack)) {
      inner.clear(); // empty selection
      return;
    }
    track = newTrack;
    length = track.getLength();
    textSelection = new TextSelection(track.getStartPosition(), length);
  }

  private static ASTNode fix(final int nodeType, final ASTNode coveredNode) {
    ASTNode $;
    for ($ = coveredNode; $ != null && $.getNodeType() != nodeType;)
      $ = $.getParent();
    return $;
  }

  private static List<WrappedCompilationUnit> asList(WrappedCompilationUnit ¢) {
    List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (¢ != null)
      $.add(¢);
    return $;
  }

  private static boolean match(ASTNode track, ASTNode newTrack) {
    return newTrack != null && (track.getClass().isInstance(newTrack) || newTrack.getClass().isInstance(track))
        && (track instanceof MethodDeclaration ? match((MethodDeclaration) track, (MethodDeclaration) newTrack)
            : track instanceof AbstractTypeDeclaration && match((AbstractTypeDeclaration) track, (AbstractTypeDeclaration) newTrack));
  }

  private static boolean match(MethodDeclaration track, MethodDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }

  private static boolean match(AbstractTypeDeclaration track, AbstractTypeDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }
}
