package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.create.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
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
  private static boolean assignmentChangable(final Assignment a, final VariableDeclarationStatement s) {
    final SimpleName var = az.simpleName(step.left(a));
    for (final VariableDeclarationFragment ¢ : step.fragments(s))
      if ((¢.getName() + "").equals(var + ""))
        return true;
    return false;
  }

  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final ForStatement ¢) {
    final ForStatement $ = duplicate.of(¢);
    $.setExpression(handleConditionInitializers(dupForExpression(¢), s));
    setInitializers($, duplicate.of(s));
    return $;
  }

  private static boolean compareModifiers(final VariableDeclarationStatement s, final VariableDeclarationExpression x) {
    final List<IExtendedModifier> l2 = step.extendedModifiers(s);
    for (final IExtendedModifier ¢ : step.extendedModifiers(x))
      if (!isIn(¢, l2))
        return false;
    return true;
  }

  private static Expression dupForExpression(final ForStatement ¢) {
    return duplicate.of(expression(¢));
  }

  private static boolean fitting(final VariableDeclarationStatement s, final ForStatement ¢) {
    return sameTypeAndModifiers(s, ¢) && fragmentsUseFitting(s, ¢) && cantTip.forRenameInitializerToCent(¢);
  }

  // TODO: now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final ForStatement s) {
    for (final VariableDeclarationFragment ¢ : step.fragments(vds))
      if (!iz.variableUsedInFor(s, ¢.getName()) || !iz.variableNotUsedAfterStatement(s, ¢.getName()))
        return false;
    return true;
  }

  private static Assignment getAssignment(final Expression ¢) {
    return az.assignment(az.parenthesizedExpression(¢).getExpression());
  }

  public static Expression handleAssignmentCondition(final Assignment from, final VariableDeclarationStatement s) {
    final SimpleName var = az.simpleName(step.left(from));
    for (final VariableDeclarationFragment ¢ : step.fragments(s))
      if ((¢.getName() + "").equals(var + "")) {
        ¢.setInitializer(duplicate.of(step.right(from)));
        return duplicate.of(step.left(from));
      }
    return from;
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression handleConditionInitializers(final Expression from, final VariableDeclarationStatement s) {
    return iz.infix(from) ? handleInfixCondition(duplicate.of(az.infixExpression(from)), s)
        : iz.assignment(from) ? handleAssignmentCondition(az.assignment(from), s)
            : iz.parenthesizedExpression(from) ? handleParenthesizedCondition(az.parenthesizedExpression(from), s) : from;
  }

  public static Expression handleInfixCondition(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression ¢ : operands) {
      if (!iz.parenthesizedExpression(¢) || !iz.assignment(az.parenthesizedExpression(¢).getExpression())
          || !assignmentChangable(getAssignment(¢), s))
        continue;
      operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(handleAssignmentCondition(getAssignment(¢), s) + ""));
    }
    return subject.append(subject.pair(operands.get(0), operands.get(1)).to(from.getOperator()), minus.firstElems(operands, 2));
  }

  public static Expression handleParenthesizedCondition(final ParenthesizedExpression from, final VariableDeclarationStatement s) {
    final Assignment a = az.assignment(from.getExpression());
    final InfixExpression e = az.infixExpression(from.getExpression());
    final ParenthesizedExpression pe = az.parenthesizedExpression(from.getExpression());
    return a != null ? handleAssignmentCondition(a, s)
        : e != null ? handleInfixCondition(e, s) : pe != null ? handleParenthesizedCondition(pe, s) : from;
  }

  private static boolean isIn(final IExtendedModifier m, final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (IExtendedModifiersOrdering.compare(m, ¢) == 0)
        return true;
    return false;
  }

  private static boolean sameTypeAndModifiers(final VariableDeclarationStatement s, final ForStatement ¢) {
    final List<Expression> initializers = step.initializers(¢);
    if (initializers.isEmpty() || !iz.variableDeclarationExpression(first(initializers)))
      return true;
    assert initializers != null;
    final Expression first = first(initializers);
    assert first != null;
    final VariableDeclarationExpression e = az.variableDeclarationExpression(first);
    return e != null && (e.getType() + "").equals(s.getType() + "") && compareModifiers(s, e);
  }

  private static void setInitializers(final ForStatement $, final VariableDeclarationStatement s) {
    final VariableDeclarationExpression oldInitializers = step.forInitializers($);
    step.initializers($).clear();
    step.initializers($).add(az.variableDeclarationExpression(s));
    step.fragments(step.forInitializers($)).addAll(duplicate.of(step.fragments(oldInitializers)));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'for' loop, rewrite as (" + ¢ + "; " + expression(az.forStatement(extract.nextStatement(¢))) + "loop";
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
    // TODO: use list rewriter; talk to Ori Roth
    r.remove(declarationStatement, g);
    r.replace(forStatement, buildForStatement(declarationStatement, forStatement), g);
    return r;
  }
}
