package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class $FragementInitializerStatement extends $Fragment {
  protected static boolean forbidden(final VariableDeclarationFragment f, final Expression initializer) {
    return initializer == null || haz.annotation(f);
  }

  /** Removes a {@link VariableDeclarationFragment}, leaving intact any other
   * fragment fragments in the containing {@link VariabelDeclarationStatement} .
   * Still, if the containing node is left empty, it is removed as well.
   * @param f
   * @param r
   * @param g */
  static void remove(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    r.remove(parent.fragments().size() > 1 ? f : parent, g);
  }

  boolean usedInSubsequentInitializers() {
    boolean searching = true;
    for (final VariableDeclarationFragment f : fragments(parent()))
      if (searching)
        searching = f != fragment();
      else if (!collect.usesOf(name()).in(f.getInitializer()).isEmpty())
        return true;
    return false;
  }

  private static final long serialVersionUID = 7723281513517888L;

  @Override public abstract String description(VariableDeclarationFragment f);

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }

  boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return remainingSiblings().stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  public int eliminationSaving() {
    final List<VariableDeclarationFragment> live = others();
    final int $ = metrics.size(parent());
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent());
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }
}
