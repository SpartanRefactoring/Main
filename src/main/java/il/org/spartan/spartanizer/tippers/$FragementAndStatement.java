package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 25, 2016 */
public abstract class $FragementAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {
  private static final long serialVersionUID = 7723281513517888L;

  @Override public boolean prerequisite(final VariableDeclarationFragment ¢) {
    return super.prerequisite(¢) && ¢ != null;
  }

  @Override public abstract String description(VariableDeclarationFragment f);

  static boolean doesUseForbiddenSiblings(final VariableDeclarationFragment f, final ASTNode... ns) {
    return forbiddenSiblings(f).stream().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }

  static int eliminationSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = wizard.live(f, fragments(parent));
    final int $ = metrics.size(parent);
    if (live.isEmpty())
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    return $ - metrics.size(newParent);
  }

  protected static boolean forbidden(final VariableDeclarationFragment f, final Expression initializer) {
    return initializer == null || haz.annotation(f);
  }

  private static Collection<VariableDeclarationFragment> forbiddenSiblings(final VariableDeclarationFragment f) {
    final Collection<VariableDeclarationFragment> $ = new ArrayList<>();
    boolean collecting = false;
    for (final VariableDeclarationFragment brother : fragments((VariableDeclarationStatement) f.getParent())) {
      if (brother == f) {
        collecting = true;
        continue;
      }
      if (collecting)
        $.add(brother);
    }
    return $;
  }

  public static int removalSaving(final VariableDeclarationFragment f) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final int $ = metrics.size(parent);
    if (parent.fragments().size() <= 1)
      return $;
    final VariableDeclarationStatement newParent = copy.of(parent);
    newParent.fragments().remove(parent.fragments().indexOf(f));
    return $ - metrics.size(newParent);
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

  static boolean usedInSubsequentInitializers(final VariableDeclarationFragment f, final SimpleName n) {
    boolean searching = true;
    for (final VariableDeclarationFragment ff : fragments(az.variableDeclrationStatement(f.getParent())))
      if (searching)
        searching = ff != f;
      else if (!collect.usesOf(n).in(ff.getInitializer()).isEmpty())
        return true;
    return false;
  }

  protected abstract ASTRewrite go(ASTRewrite r, VariableDeclarationFragment f, SimpleName n, Expression initializer, Statement nextStatement,
      TextEditGroup g);

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement,
      final TextEditGroup g) {
    if (!iz.variableDeclarationStatement(f.getParent()))
      return null;
    final SimpleName $ = f.getName();
    return $ == null ? null : go(r, f, $, f.getInitializer(), nextStatement, g);
  }

  @Override public final Tip tip(final VariableDeclarationFragment f, final ExclusionManager exclude) {
    final Tip $ = super.tip(f, exclude);
    if ($ != null && exclude != null)
      exclude.exclude(f.getParent());
    return $;
  }
}
