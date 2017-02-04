/** TODO: Ori Roth <ori.rothh@gmail.com> please add a description
 * @author Ori Roth <ori.rothh@gmail.com>
 * @since Oct 16, 2016 */
package il.org.spartan.plugin;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackerSelection extends Selection {
  ASTNode track;
  int length;

  public TrackerSelection(final WrappedCompilationUnit compilationUnit, final ITextSelection textSelection, final String name) {
    super(asList(compilationUnit), textSelection, name);
  }

  @Nullable public static TrackerSelection empty() {
    return new TrackerSelection(null, null, null);
  }

  @NotNull public TrackerSelection track(@NotNull final ASTNode ¢) {
    assert ¢ != null;
    assert ¢ instanceof MethodDeclaration || ¢ instanceof AbstractTypeDeclaration;
    track = ¢;
    length = ¢.getLength();
    return this;
  }

  public void update() {
    first(inner).dispose();
    final ASTNode newTrack = fix(track.getNodeType(),
        track.getLength() > length
            ? new NodeFinder(first(inner).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveringNode()
            : new NodeFinder(first(inner).build().compilationUnit, track.getStartPosition(), track.getLength()).getCoveredNode());
    if (!match(track, newTrack)) {
      inner.clear(); // empty selection
      return;
    }
    track = newTrack;
    length = track.getLength();
    textSelection = new TextSelection(track.getStartPosition(), length);
  }

  @NotNull private static List<WrappedCompilationUnit> asList(@Nullable final WrappedCompilationUnit ¢) {
    final List<WrappedCompilationUnit> $ = new ArrayList<>();
    if (¢ != null)
      $.add(¢);
    return $;
  }

  private static ASTNode fix(final int nodeType, final ASTNode coveredNode) {
    return yieldAncestors.untilNodeType(nodeType).from(coveredNode);
  }

  private static boolean match(@NotNull final ASTNode track, @Nullable final ASTNode newTrack) {
    return newTrack != null && (track.getClass().isInstance(newTrack) || newTrack.getClass().isInstance(track))
        && (track instanceof MethodDeclaration ? match((MethodDeclaration) track, (MethodDeclaration) newTrack)
            : track instanceof AbstractTypeDeclaration && match((AbstractTypeDeclaration) track, (AbstractTypeDeclaration) newTrack));
  }

  private static boolean match(@NotNull final MethodDeclaration track, @NotNull final MethodDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }

  private static boolean match(@NotNull final AbstractTypeDeclaration track, @NotNull final AbstractTypeDeclaration newTrack) {
    return track.getName() == null || newTrack.getName() == null ? track.getName() == null && newTrack.getName() == null
        : track.getName().getIdentifier().equals(newTrack.getName().getIdentifier());
  }
}
