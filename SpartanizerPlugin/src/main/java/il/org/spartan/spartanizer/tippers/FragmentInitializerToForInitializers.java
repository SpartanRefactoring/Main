package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code int a=3;for(;p;){++i}} to {@code for(int a=3;p;) {++i;}}
 * @author Alex Kopzon
 * @since 2016 */
public final class FragmentInitializerToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -8610595251612382642L;

  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final ForStatement ¢) {
    final ForStatement $ = copy.of(¢);
    $.setExpression(removeInitializersFromExpression(copy.of(expression(¢)), s));
    setInitializers($, copy.of(s));
    return $;
  }

  private static boolean fitting(final VariableDeclarationStatement s, final ForStatement ¢) {
    return sameTypeAndModifiers(s, ¢) && fragmentsUseFitting(s, ¢) && cantTip.forRenameInitializerToCent(¢);
  }

  /** final modifier is the only legal modifier inside a for loop, thus we push
   * initializers only if both, initializer's and declaration's modifiers lists
   * are empty, or contain final modifier only.
   * @param s
   * @param x
   * @return whether initializer's and declaration's modifiers are mergable. */
  private static boolean fittingModifiers(final VariableDeclarationStatement s, final VariableDeclarationExpression x) {
    final List<IExtendedModifier> $ = extendedModifiers(s), initializerModifiers = extendedModifiers(x);
    return $.isEmpty() && initializerModifiers.isEmpty() || haz.final¢($) && haz.final¢(initializerModifiers);
  }

  private static boolean fittingType(final VariableDeclarationStatement s, final VariableDeclarationExpression x) {
    return (x.getType() + "").equals(s.getType() + "");
  }

  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final ForStatement s) {
    return fragments(vds).stream().allMatch(λ -> Inliner.variableUsedInFor(s, name(λ)) && Inliner.variableNotUsedAfterStatement(s, name(λ)));
  }

  public static Expression handleAssignmentCondition(final Assignment from, final VariableDeclarationStatement s) {
    fragments(s).stream().filter(λ -> identifier(λ).equals(az.simpleName(left(from)) + ""))
        .forEachOrdered(λ -> λ.setInitializer(copy.of(right(from))));
    return copy.of(left(from));
  }

  public static Expression handleParenthesizedCondition(final ParenthesizedExpression from, final VariableDeclarationStatement s) {
    final Assignment $ = az.assignment(from.getExpression());
    final InfixExpression e = az.infixExpression(extract.core(from));
    return $ != null ? handleAssignmentCondition($, s) : e != null ? wizard.goInfix(e, s) : from;
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression removeInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? wizard.goInfix(az.infixExpression(from), s)
        : iz.assignment(from) ? handleAssignmentCondition(az.assignment(from), s) : from;
  }

  private static boolean sameTypeAndModifiers(final VariableDeclarationStatement s, final ForStatement ¢) {
    final List<Expression> initializers = initializers(¢);
    if (initializers.isEmpty())
      return true;
    if (!iz.variableDeclarationExpression(first(initializers)))
      return false;
    final VariableDeclarationExpression $ = az.variableDeclarationExpression(first(initializers));
    assert $ != null : "FragmentToForInitializers -> for initializer is null and not empty?!?";
    return fittingType(s, $) && fittingModifiers(s, $);
  }

  private static void setInitializers(final ForStatement $, final VariableDeclarationStatement s) {
    final VariableDeclarationExpression forInitializer = az.variableDeclarationExpression(first(initializers($)));
    initializers($).clear();
    initializers($).add(make.variableDeclarationExpression(s));
    fragments(az.variableDeclarationExpression(first(initializers($)))).addAll(copy.of(fragments(forInitializer)));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Convert 'while' into a 'for' loop, rewriting as 'for (" + ¢ + "; " + expression(az.forStatement(extract.nextStatement(¢))) + "; )' loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || $ == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement declarationStatement = az.variableDeclrationStatement(f.getParent());
    if (declarationStatement == null)
      return null;
    final ForStatement forStatement = az.forStatement(nextStatement);
    if (forStatement == null || !fitting(declarationStatement, forStatement))
      return null;
    exclude.excludeAll(fragments(declarationStatement));
    $.remove(declarationStatement, g);
    // TODO Ori Roth: use list rewriter; talk to Ori Roth
    $.replace(forStatement, buildForStatement(declarationStatement, forStatement), g);
    return $;
  }
}