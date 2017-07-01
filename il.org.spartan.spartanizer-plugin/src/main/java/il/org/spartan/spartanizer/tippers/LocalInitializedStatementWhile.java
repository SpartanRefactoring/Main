package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** convert {@code int a = 3; while(Panic) { ++OS.is.in.danger; } } to
 * {@code for(int a = 3; Panic;) { ++OS.is.in.danger; } }
 * @author Alex Kopzon
 * @since 2016 */
public final class LocalInitializedStatementWhile extends ReplaceToNextStatementExclude<VariableDeclarationFragment>//
    implements Category.Collapse {
  private static final long serialVersionUID = 0x7B0F45FEAE8DEEA5L;

  private static VariableDeclarationStatement fragmentParent(final VariableDeclarationFragment ¢) {
    return copy.of(az.variableDeclrationStatement(parent(¢)));
  }
  // TODO now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final WhileStatement s) {
    return fragments(vds).stream()
        .allMatch(λ -> collect.variableUsedInWhile(s, name(λ)) && Inliner.variableNotUsedAfterStatement(az.statement(s), λ.getName()));
  }
  public static Expression Initializers(final VariableDeclarationFragment ¢) {
    return make.variableDeclarationExpression(fragmentParent(¢));
  }
  public static VariableDeclarationStatement parent(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(step.parent(¢));
  }
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subsequent 'while', making a 'for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + ";)' loop";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    if (f == null || $ == null || nextStatement == null)
      return null;
    final VariableDeclarationStatement vds = parent(f);
    if (vds == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null || !fragmentsUseFitting(vds, s))
      return null;
    $.remove(vds, g);
    $.replace(s, make.forStatement(f, s), g);
    return $;
  }
}
