package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * for(;Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class ForToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final ForStatement ¢) {
    final ForStatement $ = duplicate.of(¢);
    $.setExpression(removeInitializersFromExpression(dupForExpression(¢), s));
    setInitializers($, duplicate.of(s));
    return $;
  }
  private static Expression dupForExpression(final ForStatement ¢) {
    return duplicate.of(expression(¢));
  }
  private static boolean fitting(final VariableDeclarationStatement s, final ForStatement ¢) {
    return sameTypeAndModifiers(s, ¢) && fragmentsUseFitting(s, ¢) && cantTip.forRenameInitializerToCent(¢);
  }
  /** final modifier is the only legal modifier inside a for loop, thus we push
   * initializers only if both, initializer's and declaration's modifiers lists
   * are empty, or contain final modifier only.
   * @param s
   * @param x
   * @return <code><b>true</b></code> <em>iff</em> initializer's and
   *         declaration's modifiers are mergable. */
  private static boolean fittingModifiers(final VariableDeclarationStatement s, final VariableDeclarationExpression x) {
    final List<IExtendedModifier> declarationModifiers = step.extendedModifiers(s), initializerModifiers = step.extendedModifiers(x);
    return declarationModifiers.isEmpty() && initializerModifiers.isEmpty() || haz.final¢(declarationModifiers) && haz.final¢(initializerModifiers);
  }
  private static boolean fittingType(final VariableDeclarationStatement s, final VariableDeclarationExpression x) {
    return (x.getType() + "").equals(s.getType() + "");
  }
  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final ForStatement s) {
    for (final VariableDeclarationFragment ¢ : step.fragments(vds))
      if (!iz.variableUsedInFor(s, ¢.getName()) || !iz.variableNotUsedAfterStatement(s, ¢.getName()))
        return false;
    return true;
  }
  public static Expression handleAssignmentCondition(final Assignment from, final VariableDeclarationStatement s) {
    final SimpleName var = az.simpleName(step.left(from));
    for (final VariableDeclarationFragment ¢ : step.fragments(s))
      if ((¢.getName() + "").equals(var + ""))
        ¢.setInitializer(duplicate.of(step.right(from)));
    return duplicate.of(step.left(from));
  }
  public static Expression handleInfixCondition(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression x : operands)
      if (iz.parenthesizedExpression(x) && iz.assignment(az.parenthesizedExpression(x).getExpression())) {
        final Assignment a = az.assignment(az.parenthesizedExpression(x).getExpression());
        final SimpleName var = az.simpleName(step.left(a));
        for (final VariableDeclarationFragment ¢ : fragments(s))
          if ((¢.getName() + "").equals(var + "")) {
            ¢.setInitializer(duplicate.of(step.right(a)));
            operands.set(operands.indexOf(x), x.getAST().newSimpleName(var + ""));
          }
      }
    return subject.append(subject.pair(operands.get(0), operands.get(1)).to(from.getOperator()), chop(chop(operands)));
  }
  public static Expression handleParenthesizedCondition(final ParenthesizedExpression from, final VariableDeclarationStatement s) {
    final Assignment a = az.assignment(from.getExpression());
    final InfixExpression e = az.infixExpression(from.getExpression());
    final ParenthesizedExpression pe = az.parenthesizedExpression(from.getExpression());
    return a != null ? handleAssignmentCondition(a, s)
        : e != null ? handleInfixCondition(e, s) : pe != null ? handleParenthesizedCondition(pe, s) : from;
  }
  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression removeInitializersFromExpression(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? handleInfixCondition(duplicate.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from) ? handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }
  private static boolean sameTypeAndModifiers(final VariableDeclarationStatement s, final ForStatement ¢) {
    final List<Expression> initializers = step.initializers(¢);
    if (initializers.isEmpty() || !iz.variableDeclarationExpression(first(initializers)))
      return true;
    final VariableDeclarationExpression e = az.variableDeclarationExpression(first(initializers));
    assert e != null : "ForToForInitializers -> for initializer is null and not empty?!?";
    return fittingType(s, e) && fittingModifiers(s, e);
  }
  private static void setInitializers(final ForStatement $, final VariableDeclarationStatement s) {
    final VariableDeclarationExpression forInitializer = az.variableDeclarationExpression(findFirst.elementOf(step.initializers($)));
    step.initializers($).clear();
    step.initializers($).add(az.variableDeclarationExpression(s));
    step.fragments(az.variableDeclarationExpression(findFirst.elementOf(step.initializers($)))).addAll(duplicate.of(step.fragments(forInitializer)));
  }
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Convert 'while' into a 'for' loop, rewriting as 'for (" + ¢ + "; " + expression(az.forStatement(extract.nextStatement(¢))) + "; )' loop";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || r == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement declarationStatement = az.variableDeclrationStatement(f.getParent());
    if (declarationStatement == null)
      return null;
    final ForStatement forStatement = az.forStatement(nextStatement);
    if (forStatement == null || !fitting(declarationStatement, forStatement))
      return null;
    exclude.excludeAll(step.fragments(declarationStatement));
    // TODO Ori Roth: use list rewriter; talk to Ori Roth
    r.remove(declarationStatement, g);
    r.replace(forStatement, buildForStatement(declarationStatement, forStatement), g);
    return r;
  }
}