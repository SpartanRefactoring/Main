package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Pushdown a ternary as far down as possible
 * @author Yossi Gil
 * @year 2015 */
public final class TernaryPushdown extends ReplaceCurrentNode<ConditionalExpression> implements Kind.CommnoFactoring {
  public static Expression right(final Assignment a1) {
    return a1.getRightHandSide();
  }

  static Expression pushdown(final ConditionalExpression e) {
    if (e == null)
      return null;
    final Expression then = core(e.getThenExpression());
    final Expression elze = core(e.getElseExpression());
    return wizard.same(then, elze) ? null : pushdown(e, then, elze);
  }

  static Expression pushdown(final ConditionalExpression e, final Assignment a1, final Assignment a2) {
    return a1.getOperator() != a2.getOperator() || !wizard.same(to(a1), to(a2)) ? null
        : make.plant(subject.pair(to(a1), subject.pair(right(a1), right(a2)).toCondition(e.getExpression())).to(a1.getOperator()))
            .into(e.getParent());
  }

  private static int findSingleDifference(final List<Expression> es1, final List<Expression> es2) {
    int $ = -1;
    for (int ¢ = 0; ¢ < es1.size(); ++¢)
      if (!wizard.same(es1.get(¢), es2.get(¢))) {
        if ($ >= 0)
          return -1;
        $ = ¢;
      }
    return $;
  }

  @SuppressWarnings("unchecked") private static <T extends Expression> T p(final ASTNode n, final T $) {
    return !precedence.is.legal(precedence.of(n)) || precedence.of(n) >= precedence.of($) ? $ : (T) wizard.parenthesize($);
  }

  private static Expression pushdown(final ConditionalExpression e, final ClassInstanceCreation e1, final ClassInstanceCreation e2) {
    if (!wizard.same(e1.getType(), e2.getType()) || !wizard.same(e1.getExpression(), e2.getExpression()))
      return null;
    final List<Expression> es1 = arguments(e1);
    final List<Expression> es2 = arguments(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final ClassInstanceCreation $ = duplicate.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }

  private static Expression pushdown(final ConditionalExpression e, final Expression e1, final Expression e2) {
    if (e1.getNodeType() != e2.getNodeType())
      return null;
    switch (e1.getNodeType()) {
      case SUPER_METHOD_INVOCATION:
        return pushdown(e, (SuperMethodInvocation) e1, (SuperMethodInvocation) e2);
      case METHOD_INVOCATION:
        return pushdown(e, (MethodInvocation) e1, (MethodInvocation) e2);
      case INFIX_EXPRESSION:
        return pushdown(e, (InfixExpression) e1, (InfixExpression) e2);
      case ASSIGNMENT:
        return pushdown(e, (Assignment) e1, (Assignment) e2);
      case FIELD_ACCESS:
        return pushdown(e, (FieldAccess) e1, (FieldAccess) e2);
      case CLASS_INSTANCE_CREATION:
        return pushdown(e, (ClassInstanceCreation) e1, (ClassInstanceCreation) e2);
      default:
        return null;
    }
  }

  private static Expression pushdown(final ConditionalExpression e, final FieldAccess e1, final FieldAccess e2) {
    if (!wizard.same(e1.getName(), e2.getName()))
      return null;
    final FieldAccess $ = duplicate.of(e1);
    $.setExpression(wizard.parenthesize(subject.pair(e1.getExpression(), e2.getExpression()).toCondition(e.getExpression())));
    return $;
  }

  private static Expression pushdown(final ConditionalExpression e, final InfixExpression e1, final InfixExpression e2) {
    if (e1.getOperator() != e2.getOperator())
      return null;
    final List<Expression> es1 = hop.operands(e1);
    final List<Expression> es2 = hop.operands(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final InfixExpression $ = duplicate.of(e1);
    final List<Expression> operands = hop.operands($);
    operands.remove(i);
    operands.add(i, p($, subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression())));
    return p(e, subject.operands(operands).to($.getOperator()));
  }

  private static Expression pushdown(final ConditionalExpression e, final MethodInvocation e1, final MethodInvocation e2) {
    if (!wizard.same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = arguments(e1);
    final List<Expression> es2 = arguments(e2);
    final Expression receiver1 = e1.getExpression();
    final Expression receiver2 = e2.getExpression();
    if (!wizard.same(receiver1, receiver2)) {
      if (receiver1 == null || receiver2 == null || !wizard.same(es1, es2) || NameGuess.isClassName(receiver1) || NameGuess.isClassName(receiver2))
        return null;
      final MethodInvocation $ = duplicate.of(e1);
      assert $ != null;
      $.setExpression(wizard.parenthesize(subject.pair(receiver1, receiver2).toCondition(e.getExpression())));
      return $;
    }
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final MethodInvocation $ = duplicate.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }

  private static Expression pushdown(final ConditionalExpression e, final SuperMethodInvocation e1, final SuperMethodInvocation e2) {
    if (!wizard.same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = arguments(e1);
    final List<Expression> es2 = arguments(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final SuperMethodInvocation $ = duplicate.of(e1);
    arguments($).remove(i);
    arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Pushdown ?: into expression";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return pushdown(¢);
  }
}
