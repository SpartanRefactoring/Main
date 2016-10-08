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
import il.org.spartan.spartanizer.engine.*;
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
  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final ForStatement ¢) {
    final ForStatement $ = duplicate.of(¢);
    $.setExpression(removeInitializersFromExpression(dupForExpression(¢), s));
    setInitializers($, duplicate.of(s));
    return $;
  }

  private static boolean containsModifiers(final List<IExtendedModifier> contained, final List<IExtendedModifier> contains) {
    for (final IExtendedModifier ¢ : contained)
      if (!isIn(¢, contains))
        return false;
    return true;
  }

  private static Expression dupForExpression(final ForStatement ¢) {
    return duplicate.of(expression(¢));
  }

  private static boolean fitting(final VariableDeclarationStatement s, final ForStatement ¢) {
    return sameTypeAndModifiers(s, ¢) && fragmentsUseFitting(s, ¢) && cantTip.forRenameInitializerToCent(¢);
  }

  // TODO: Alex and Dan, now fitting returns true iff all fragments fitting. We
  // may want to be able to treat each fragment separately.
  private static boolean fragmentsUseFitting(final VariableDeclarationStatement vds, final ForStatement s) {
    for (final VariableDeclarationFragment ¢ : step.fragments(vds))
      if (!variableUsedInFor(s, ¢.getName()) || !iz.variableNotUsedAfterStatement(s, ¢.getName()))
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

  /** XXX: This is a bug in autospartanization [[SuppressWarningsSpartan]] */
  public static Expression handleInfixCondition(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression ¢¢ : operands)
      if (iz.parenthesizedExpression(¢¢) && iz.assignment(az.parenthesizedExpression(¢¢).getExpression())) {
        final Assignment a = az.assignment(az.parenthesizedExpression(¢¢).getExpression());
        final SimpleName var = az.simpleName(step.left(a));
        for (final VariableDeclarationFragment f : fragments(s))
          if ((f.getName() + "").equals(var + "")) {
            f.setInitializer(duplicate.of(step.right(a)));
            operands.set(operands.indexOf(¢¢), ¢¢.getAST().newSimpleName(var + ""));
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

  private static boolean isIn(final IExtendedModifier m, final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (IExtendedModifiersOrdering.compare(m, ¢) == 0)
        return true;
    return false;
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
    final List<IExtendedModifier> extendedModifiers = step.extendedModifiers(e);
    final List<IExtendedModifier> extendedModifiers2 = step.extendedModifiers(s);
    return extendedModifiers2 != extendedModifiers && extendedModifiers != null && extendedModifiers2 != null //
        && (e.getType() + "").equals(s.getType() + "") && //
        containsModifiers(extendedModifiers, extendedModifiers2) && containsModifiers(extendedModifiers2,extendedModifiers);
  }

  private static void setInitializers(final ForStatement $, final VariableDeclarationStatement s) {
    final VariableDeclarationExpression forInitializer = az.variableDeclarationExpression(findFirst.elementOf(step.initializers($)));
    step.initializers($).clear();
    step.initializers($).add(az.variableDeclarationExpression(s));
    step.fragments(az.variableDeclarationExpression(findFirst.elementOf(step.initializers($)))).addAll(duplicate.of(step.fragments(forInitializer)));
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return true <b>iff</b> the SimpleName is used in a ForStatement's
   *         condition, updaters, or body. */
  private static boolean variableUsedInFor(final ForStatement s, final SimpleName n) {
    if (!Collect.usesOf(n).in(step.condition(s)).isEmpty() || !Collect.usesOf(n).in(step.body(s)).isEmpty())
      return true;
    for (final Expression ¢ : step.updaters(s))
      if (!Collect.usesOf(n).in(¢).isEmpty())
        return true;
    return false;
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
    // TODO: Alex use list rewriter; talk to Ori Roth
    r.remove(declarationStatement, g);
    r.replace(forStatement, buildForStatement(declarationStatement, forStatement), g);
    return r;
  }
}