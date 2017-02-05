package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;



/** Pushdown a ternary as far down as possible
 * @year 2015
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public final class TernaryPushdown extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.CommnonFactoring {
   static Expression pushdown( final ConditionalExpression x) {
    if (x == null)
      return null;
    final Expression $ = core(then(x)), elze = core(elze(x));
    return wizard.same($, elze) ? null : pushdown(x, $, elze);
  }

  static Expression pushdown( final ConditionalExpression x, final Assignment a1, final Assignment a2) {
    return operator(a1) != operator(a2) || !wizard.same(to(a1), to(a2)) ? null
        : make.plant(subject.pair(to(a1), subject.pair(right(a1), right(a2)).toCondition(expression(x))).to(operator(a1))).into(x.getParent());
  }

  @SuppressWarnings("unchecked")  private static <T extends Expression> T p(final ASTNode n,  final T $) {
    return !precedence.is.legal(precedence.of(n)) || precedence.of(n) >= precedence.of($) ? $ : (T) wizard.parenthesize($);
  }

  private static Expression pushdown(final ConditionalExpression x, final ClassInstanceCreation e1, final ClassInstanceCreation e2) {
    if (!wizard.same(type(e1), type(e2)) || !wizard.same(expression(e1), expression(e2)))
      return null;
    final List<Expression> es1 = arguments(e1), es2 = arguments(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final ClassInstanceCreation $ = copy.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(expression(x)));
    return $;
  }

  private static Expression pushdown( final ConditionalExpression x,  final Expression e1,  final Expression e2) {
    if (e1.getNodeType() != e2.getNodeType())
      return null;
    switch (e1.getNodeType()) {
      case ASSIGNMENT:
        return pushdown(x, (Assignment) e1, (Assignment) e2);
      case CLASS_INSTANCE_CREATION:
        return pushdown(x, (ClassInstanceCreation) e1, (ClassInstanceCreation) e2);
      case FIELD_ACCESS:
        return pushdown(x, (FieldAccess) e1, (FieldAccess) e2);
      case INFIX_EXPRESSION:
        return pushdown(x, (InfixExpression) e1, (InfixExpression) e2);
      case METHOD_INVOCATION:
        return pushdown(x, (MethodInvocation) e1, (MethodInvocation) e2);
      case SUPER_METHOD_INVOCATION:
        return pushdown(x, (SuperMethodInvocation) e1, (SuperMethodInvocation) e2);
      default:
        return null;
    }
  }

  private static Expression pushdown(final ConditionalExpression x, final FieldAccess e1, final FieldAccess e2) {
    if (!wizard.same(name(e1), name(e2)))
      return null;
    final FieldAccess $ = copy.of(e1);
    $.setExpression(wizard.parenthesize(subject.pair(expression(e1), expression(e2)).toCondition(expression(x))));
    return $;
  }

  private static Expression pushdown(final ConditionalExpression x, final InfixExpression e1, final InfixExpression e2) {
    if (operator(e1) != operator(e2))
      return null;
    final List<Expression> es1 = hop.operands(e1), es2 = hop.operands(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final InfixExpression $ = copy.of(e1);
    final List<Expression> operands = hop.operands($);
    operands.remove(i);
    operands.add(i, p($, subject.pair(es1.get(i), es2.get(i)).toCondition(expression(x))));
    return p(x, subject.operands(operands).to($.getOperator()));
  }

  private static Expression pushdown(final ConditionalExpression x,  final MethodInvocation e1,  final MethodInvocation e2) {
    if (!wizard.same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = arguments(e1), es2 = arguments(e2);
    final Expression receiver1 = expression(e1), receiver2 = expression(e2);
    if (!wizard.same(receiver1, receiver2)) {
      if (receiver1 == null || receiver2 == null || !wizard.same(es1, es2) || guessName.isClassName(receiver1) || guessName.isClassName(receiver2))
        return null;
      final MethodInvocation $ = copy.of(e1);
      assert $ != null;
      $.setExpression(wizard.parenthesize(subject.pair(receiver1, receiver2).toCondition(expression(x))));
      return $;
    }
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final MethodInvocation $ = copy.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(expression(x)));
    return $;
  }

  private static Expression pushdown(final ConditionalExpression x,  final SuperMethodInvocation e1,
       final SuperMethodInvocation e2) {
    if (!wizard.same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = arguments(e1), es2 = arguments(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final SuperMethodInvocation $ = copy.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(expression(x)));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Pushdown ?: into expression";
  }

  @Override  public Expression replacement(final ConditionalExpression ¢) {
    return pushdown(¢);
  }
}
