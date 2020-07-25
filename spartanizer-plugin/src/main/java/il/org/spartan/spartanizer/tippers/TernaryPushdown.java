package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.operator;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static il.org.spartan.spartanizer.ast.navigate.step.type;
import static il.org.spartan.spartanizer.ast.navigate.wizard.eq;
import static il.org.spartan.spartanizer.ast.navigate.wizard.findSingleDifference;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CLASS_INSTANCE_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_INVOCATION;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.engine.nominal.guessName;
import il.org.spartan.spartanizer.java.precedence;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Pushdown a ternary as far down as possible
 * @year 2015
 * @author Yossi Gil
 * @since Sep 25, 2016 */
public final class TernaryPushdown extends ReplaceCurrentNode<ConditionalExpression>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x77C40E8DAFC91AFBL;

  static Expression pushdown(final ConditionalExpression x) {
    if (x == null)
      return null;
    final Expression $ = core(then(x)), elze = core(elze(x));
    return eq($, elze) ? null : pushdown(x, $, elze);
  }
  static Expression pushdown(final ConditionalExpression x, final Assignment a1, final Assignment a2) {
    return operator(a1) != operator(a2) || !eq(to(a1), to(a2)) ? null
        : make.plant(subject.pair(to(a1), subject.pair(right(a1), right(a2)).toCondition(expression(x))).to(operator(a1))).into(x.getParent());
  }
  @SuppressWarnings("unchecked") private static <T extends Expression> T p(final ASTNode n, final T $) {
    return !precedence.isLegal(precedence.of(n)) || precedence.of(n) >= precedence.of($) ? $ : (T) misc.parenthesize($);
  }
  private static Expression pushdown(final ConditionalExpression x, final ClassInstanceCreation e1, final ClassInstanceCreation e2) {
    if (!eq(type(e1), type(e2)) || !eq(expression(e1), expression(e2)))
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
  private static Expression pushdown(final ConditionalExpression x, final Expression e1, final Expression e2) {
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
    if (!eq(name(e1), name(e2)))
      return null;
    final FieldAccess $ = copy.of(e1);
    $.setExpression(misc.parenthesize(subject.pair(expression(e1), expression(e2)).toCondition(expression(x))));
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
  private static Expression pushdown(final ConditionalExpression x, final MethodInvocation e1, final MethodInvocation e2) {
    if (!eq(e1.getName(), e2.getName()))
      return null;
    final List<Expression> es1 = arguments(e1), es2 = arguments(e2);
    final Expression receiver1 = expression(e1), receiver2 = expression(e2);
    final MethodInvocation $ = copy.of(e1);
    if (!eq(receiver1, receiver2)) {
      if (receiver1 == null || receiver2 == null || !eq(es1, es2) || guessName.isClassName(receiver1) || guessName.isClassName(receiver2))
        return null;
      assert $ != null;
      $.setExpression(misc.parenthesize(subject.pair(receiver1, receiver2).toCondition(expression(x))));
    } else {
      if (es1.size() != es2.size())
        return null;
      final int i = findSingleDifference(es1, es2);
      if (i < 0)
        return null;
      arguments($).remove(i);
      arguments($).add(i, subject.pair(es1.get(i), es2.get(i)).toCondition(expression(x)));
    }
    return $;
  }
  private static Expression pushdown(final ConditionalExpression x, final SuperMethodInvocation e1, final SuperMethodInvocation e2) {
    if (!eq(e1.getName(), e2.getName()))
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
  @Override public Expression replacement(final ConditionalExpression ¢) {
    return pushdown(¢);
  }
}
