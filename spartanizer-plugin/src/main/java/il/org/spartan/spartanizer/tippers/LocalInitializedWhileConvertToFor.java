package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.tipping.ReplaceToNextStatementExclude;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code int a = 3; while(Panic) { ++OS.is.in.danger; } } to
 * {@code for(int a = 3; Panic;) { ++OS.is.in.danger; } }
 * @author Alex Kopzon
 * @since 2016 */
public final class LocalInitializedWhileConvertToFor extends ReplaceToNextStatementExclude<VariableDeclarationFragment>//
    implements Category.Loops {
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
    return cons.variableDeclarationExpression(fragmentParent(¢));
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
