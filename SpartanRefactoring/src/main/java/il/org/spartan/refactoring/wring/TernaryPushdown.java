package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.utils.Restructure.parenthesize;

import java.util.List;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

final class TernaryPushdown extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  private static int findSingleDifference(final List<Expression> es1, final List<Expression> es2) {
    int $ = -1;
    for (int i = 0; i < es1.size(); ++i)
      if (!same(es1.get(i), es2.get(i))) {
        if ($ >= 0)
          return -1;
        $ = i;
      }
    return $;
  }
  @SuppressWarnings("unchecked") private static <T extends Expression> T p(final ASTNode n, final T $) {
    return !Precedence.Is.legal(Precedence.of(n)) || Precedence.of(n) >= Precedence.of($) ? $ : (T) parenthesize($);
  }
  private static Expression pushdown(final ConditionalExpression e, final ClassInstanceCreation e1, final ClassInstanceCreation e2) {
    if (!same(e1.getType(), e2.getType()) || !same(e1.getExpression(), e2.getExpression()))
      return null;
    final List<Expression> es1 = e1.arguments();
    final List<Expression> es2 = e2.arguments();
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final ClassInstanceCreation $ = duplicate(e1);
    $.arguments().remove(i);
    $.arguments().add(i, Subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }
  private static Expression pushdown(final ConditionalExpression e, final FieldAccess e1, final FieldAccess e2) {
    if (!same(e1.getName(), e2.getName()))
      return null;
    final FieldAccess $ = duplicate(e1);
    $.setExpression(parenthesize(Subject.pair(e1.getExpression(), e2.getExpression()).toCondition(e.getExpression())));
    return $;
  }
  private static Expression pushdown(final ConditionalExpression e, final InfixExpression e1, final InfixExpression e2) {
    if (e1.getOperator() != e2.getOperator())
      return null;
    final List<Expression> es1 = Extract.operands(e1);
    final List<Expression> es2 = Extract.operands(e2);
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final InfixExpression $ = duplicate(e1);
    final List<Expression> operands = Extract.operands($);
    operands.remove(i);
    operands.add(i, p($, Subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression())));
    return p(e, Subject.operands(operands).to($.getOperator()));
  }
  private static Expression pushdown(final ConditionalExpression e, final MethodInvocation e1, final MethodInvocation e2) {
    if (!same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = e1.arguments();
    final List<Expression> es2 = e2.arguments();
    final Expression receiver1 = e1.getExpression();
    final Expression receiver2 = e2.getExpression();
    if (!same(receiver1, receiver2)) {
      if (receiver1 == null || !same(es1, es2))
        return null;
      final ConditionalExpression c = Subject.pair(receiver1, receiver2).toCondition(e.getExpression());
      final MethodInvocation $ = duplicate(e1);
      $.setExpression(parenthesize(c));
      return $;
    }
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final MethodInvocation $ = duplicate(e1);
    $.arguments().remove(i);
    $.arguments().add(i, Subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }
  private static Expression pushdown(final ConditionalExpression e, final SuperMethodInvocation e1, final SuperMethodInvocation e2) {
    if (!same(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = e1.arguments();
    final List<Expression> es2 = e2.arguments();
    if (es1.size() != es2.size())
      return null;
    final int i = findSingleDifference(es1, es2);
    if (i < 0)
      return null;
    final SuperMethodInvocation $ = duplicate(e1);
    $.arguments().remove(i);
    $.arguments().add(i, Subject.pair(es1.get(i), es2.get(i)).toCondition(e.getExpression()));
    return $;
  }
  static Expression pushdown(final ConditionalExpression e, final Assignment a1, final Assignment a2) {
    return a1.getOperator() != a2.getOperator() || !same(left(a1), left(a2)) ? null
        : new Plant(Subject.pair(left(a1), Subject.pair(right(a1), right(a2)).toCondition(e.getExpression())).to(a1.getOperator())).into(e.getParent());
  }
  public static Expression right(final Assignment a1) {
    return a1.getRightHandSide();
  }
  static Expression pushdown(final ConditionalExpression e) {
    if (e == null)
      return null;
    final Expression then = core(e.getThenExpression());
    final Expression elze = core(e.getElseExpression());
    return same(then, elze) ? null : pushdown(e, then, elze);
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
  @Override Expression replacement(final ConditionalExpression e) {
    return pushdown(e);
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return pushdown(e) != null;
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Pushdown ?: into expression";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.REORDER_EXPRESSIONS;
  }
}