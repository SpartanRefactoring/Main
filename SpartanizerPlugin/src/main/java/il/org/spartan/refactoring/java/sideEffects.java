package il.org.spartan.refactoring.java;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.ast.extract.*;
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
  // VIM: /{/+,/}/-!sort -u
  private static final int[] alwaysFree = { //
      BOOLEAN_LITERAL, //
      CHARACTER_LITERAL, //
      CAST_EXPRESSION, //
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

  public static boolean free(final Expression x) {
    if (x == null || iz.is(x, alwaysFree))
      return true;
    if (iz.is(x, alwaysHave))
      return false;
    switch (x.getNodeType()) {
      case ARRAY_CREATION:
        return free((ArrayCreation) x);
      case ARRAY_ACCESS:
        return free(((ArrayAccess) x).getArray(), ((ArrayAccess) x).getIndex());
      case CAST_EXPRESSION:
        return free(step.expression((CastExpression) x));
      case INSTANCEOF_EXPRESSION:
        return free(step.left((InstanceofExpression) x));
      case PREFIX_EXPRESSION:
        return free((PrefixExpression) x);
      case PARENTHESIZED_EXPRESSION:
        return free(core(x));
      case INFIX_EXPRESSION:
        return free(extract.allOperands((InfixExpression) x));
      case CONDITIONAL_EXPRESSION:
        return freeConditionalExpression(az.conditionalExpression(x));
      case ARRAY_INITIALIZER:
        return free(((ArrayInitializer) x).expressions());
      default:
        throw new RuntimeException("Missing handler for class: " + x.getClass().getSimpleName());
    }
  }

  public static boolean freeConditionalExpression(final ConditionalExpression x) {
    return free(expression(x), then(x), elze(x));
  }

  public static boolean sideEffectFreeArrayCreation(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return free(c.dimensions()) && (i == null || free(i.expressions()));
  }

  public static boolean sideEffectFreePrefixExpression(final PrefixExpression x) {
    return in(x.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS, PrefixExpression.Operator.COMPLEMENT,
        PrefixExpression.Operator.NOT) && free(step.operand(x));
  }

  public static boolean deterministic(final Expression x) {
    if (!free(x))
      return false;
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.TRUE);
    x.accept(new ASTVisitor() {
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

  private static boolean free(final PrefixExpression x) {
    return in(x.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS, PrefixExpression.Operator.COMPLEMENT,
        PrefixExpression.Operator.NOT) && free(step.operand(x));
  }
}
