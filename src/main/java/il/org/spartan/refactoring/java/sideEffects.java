package il.org.spartan.refactoring.java;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;

public enum sideEffects {
  ;
  /** Determine whether the evaluation of an expression is guaranteed to be free
   * of any side effects.
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose computation is guaranteed to be free of any side effects. */
  private static final int[] alwaysFree = { //
      STRING_LITERAL, //
      NULL_LITERAL, //
      NUMBER_LITERAL, //
      THIS_EXPRESSION, //
      SIMPLE_NAME, //
      TYPE_LITERAL, //
      CHARACTER_LITERAL, //
      BOOLEAN_LITERAL, //
      QUALIFIED_NAME, //
      FIELD_ACCESS, //
      SUPER_FIELD_ACCESS,//
  };
  private static final int[] alwaysHave = { //
      SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, ASSIGNMENT, POSTFIX_EXPRESSION, };

  public static boolean free(final Expression e) {
    if (e == null || iz.is(e, alwaysFree))
      return true;
    if (iz.is(e, alwaysHave))
      return false;
    switch (e.getNodeType()) {
      case ARRAY_CREATION:
        return free((ArrayCreation) e);
      case ARRAY_ACCESS:
        return free(((ArrayAccess) e).getArray(), ((ArrayAccess) e).getIndex());
      case CAST_EXPRESSION:
        return free(step.expression(((CastExpression) e)));
      case INSTANCEOF_EXPRESSION:
        return free(step.left((InstanceofExpression) e));
      case PREFIX_EXPRESSION:
        return free((PrefixExpression) e);
      case PARENTHESIZED_EXPRESSION:
        return free(extract.core(e));
      case INFIX_EXPRESSION:
        return free(extract.allOperands((InfixExpression) e));
      case CONDITIONAL_EXPRESSION:
        return freeConditionalExpression(az.conditionalExpression(e));
      case ARRAY_INITIALIZER:
        return free(((ArrayInitializer) e).expressions());
      default:
        throw new RuntimeException("Missing handler for class: " + e.getClass().getSimpleName());
    }
  }

  public static boolean freeConditionalExpression(final ConditionalExpression e) {
    return free(expression(e), then(e), elze(e));
  }

  public static boolean sideEffectFreeArrayCreation(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return free(c.dimensions()) && (i == null || free(i.expressions()));
  }

  public static boolean sideEffectFreePrefixExpression(final PrefixExpression e) {
    return in(e.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS, PrefixExpression.Operator.COMPLEMENT,
        PrefixExpression.Operator.NOT) && free(step.operand(e));
  }

  public static boolean deterministic(final Expression e) {
    if (!free(e))
      return false;
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.TRUE);
    e.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation __) {
        $.set(Boolean.FALSE);
        return false;
      }
    });
    return $.get().booleanValue();
  }

  private static boolean free(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return free(c.dimensions()) && (i == null || free(step.expressions(i)));
  }

  private static boolean free(final Expression... es) {
    for (final Expression e : es)
      if (!free(e))
        return false;
    return true;
  }

  private static boolean free(final List<?> os) {
    for (final Object o : os)
      if (o == null || !free(az.expression((ASTNode) o)))
        return false;
    return true;
  }

  private static boolean free(final PrefixExpression e) {
    return in(e.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS, PrefixExpression.Operator.COMPLEMENT,
        PrefixExpression.Operator.NOT) && free(step.operand(e));
  }
}
