/* TODO Ori Roth <ori.rothh@gmail.com> please add a description
 *
 * @author Ori Roth <ori.rothh@gmail.com>
 *
 * @since Oct 16, 2016 */
package il.org.spartan.spartanizer.plugin;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import nano.ly.*;

public class TrackerSelection extends Selection {
  ASTNode track;
  int length;

  public TrackerSelection(final WrappedCompilationUnit compilationUnit, final ITextSelection textSelection, final String name) {
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
    the.headOf(inner).dispose();
    final ASTNode newTrack = fix(track.getNodeType(),
        track.getLength() > length
            ? new NodeFinder(the.headOf(inner).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveringNode()
            : new NodeFinder(the.headOf(inner).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveredNode());
    if (!match(track, newTrack)) {
      inner.clear(); // empty selection
      return;
    }
    track = newTrack;
    length = track.getLength();
    textSelection = new TextSelection(track.getStartPosition(), length);
  }

  private static List<WrappedCompilationUnit> asList(final WrappedCompilationUnit ¢) {
    final List<WrappedCompilationUnit> $ = an.empty.list();
    if (¢ != null)
      $.add(¢);
    return $;
  }

  private static ASTNode fix(final int nodeType, final ASTNode coveredNode) {
    return yieldAncestors.untilNodeType(nodeType).from(coveredNode);
  }

  private static boolean match(final ASTNode track, final ASTNode newTrack) {
    return newTrack != null && (track.getClass().isInstance(newTrack) || newTrack.getClass().isInstance(track))
        && (track instanceof MethodDeclaration ? match((MethodDeclaration) track, (MethodDeclaration) newTrack)
            : track instanceof AbstractTypeDeclaration && match((AbstractTypeDeclaration) track, (AbstractTypeDeclaration) newTrack));
  }

  private static boolean match(final MethodDeclaration track, final MethodDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }

  private static boolean match(final AbstractTypeDeclaration track, final AbstractTypeDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }
}
