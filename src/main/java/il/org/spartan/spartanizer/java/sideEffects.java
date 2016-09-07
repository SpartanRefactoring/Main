package il.org.spartan.spartanizer.java;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;

public enum sideEffects {
  ;
  /** Determine whether the evaluation of an expression is guaranteed to be free
   * of any side effects.
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose computation is guaranteed to be free of any side effects. */
  // VIM: /{/+,/}/-!sort -u
  private static final int[] alwaysFree = { //
      BOOLEAN_LITERAL, //
      CHARACTER_LITERAL, //
      EMPTY_STATEMENT, //
      FIELD_ACCESS, //
      NULL_LITERAL, //
      NUMBER_LITERAL, //
      PRIMITIVE_TYPE, //
      QUALIFIED_NAME, //
      SIMPLE_NAME, //
      SIMPLE_TYPE, //
      STRING_LITERAL, //
      SUPER_FIELD_ACCESS, //
      THIS_EXPRESSION, //
      TYPE_LITERAL, //
  };
  private static final int[] alwaysHave = { //
      SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, ASSIGNMENT, POSTFIX_EXPRESSION, };

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
        return free(step.expression(e));
      case INSTANCEOF_EXPRESSION:
        return free(step.left((InstanceofExpression) e));
      case PREFIX_EXPRESSION:
        return free((PrefixExpression) e);
      case PARENTHESIZED_EXPRESSION:
        return free(core(e));
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

  private static boolean free(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return free(c.dimensions()) && (i == null || free(step.expressions(i)));
  }

  private static boolean free(final Expression... xs) {
    for (final Expression e : xs)
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
