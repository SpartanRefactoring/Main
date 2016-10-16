package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

public class TrackerSelection extends Selection {
  ASTNode track;
  int length;

  public TrackerSelection(CU compilationUnit, ITextSelection textSelection, String name) {
    super(asList(compilationUnit), textSelection, name);
  }

  public static TrackerSelection empty() {
    return new TrackerSelection(null, null, null);
  }

  public TrackerSelection track(final ASTNode ¢) {
    assert MethodDeclaration.class.equals(¢.getClass()) || AbstractTypeDeclaration.class.equals(¢.getClass());
    track = ¢;
    length = ¢.getLength();
    return this;
  }

  // TODO Roth: check safety of tracking
  public void update() {
    compilationUnits.get(0).dispose();
    final ASTNode newTrack = length < track.getLength()
        ? new NodeFinder(compilationUnits.get(0).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveringNode()
        : new NodeFinder(compilationUnits.get(0).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveredNode();
    if (!sameTrack(newTrack)) {
      compilationUnits.clear(); // empty selection
      return;
    }
    track = newTrack;
    length = newTrack.getLength();
    textSelection = new TextSelection(track.getStartPosition(), track.getLength());
  }

  private boolean sameTrack(final ASTNode newTrack) {
    return newTrack != null && track.getClass().equals(newTrack.getClass())
        && (!(track instanceof MethodDeclaration) || matchNames((MethodDeclaration) track, (MethodDeclaration) newTrack))
        && (!(track instanceof AbstractTypeDeclaration) || matchNames((AbstractTypeDeclaration) track, (AbstractTypeDeclaration) newTrack))
        && track.getStartPosition() == newTrack.getStartPosition();
  }

  private static boolean matchNames(MethodDeclaration track, MethodDeclaration newTrack) {
    return track.getName() == null ? newTrack.getName() == null : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }

  private static boolean matchNames(AbstractTypeDeclaration track, AbstractTypeDeclaration newTrack) {
    return track.getName() == null ? newTrack.getName() == null : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }

  private static List<CU> asList(CU ¢) {
    List<CU> $ = new ArrayList<>();
    if (¢ != null)
      $.add(¢);
    return $;
  }
}
