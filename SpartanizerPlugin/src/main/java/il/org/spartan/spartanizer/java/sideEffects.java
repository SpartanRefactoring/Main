package il.org.spartan.spartanizer.java;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

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
      SUPER_CONSTRUCTOR_INVOCATION, //
      SUPER_METHOD_INVOCATION, //
      METHOD_INVOCATION, //
      CLASS_INSTANCE_CREATION, //
      ASSIGNMENT, //
      POSTFIX_EXPRESSION, //
  };

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

  public static boolean free(final ConditionalExpression x) {
    return free(expression(x), then(x), elze(x));
  }

  public static boolean free(final Expression ¢) {
    if (¢ == null || iz.is(¢, alwaysFree))
      return true;
    if (iz.is(¢, alwaysHave))
      return false;
    switch (¢.getNodeType()) {
      case ARRAY_CREATION:
        return free((ArrayCreation) ¢);
      case ARRAY_ACCESS:
        return free(((ArrayAccess) ¢).getArray(), ((ArrayAccess) ¢).getIndex());
      case CAST_EXPRESSION:
        return free(step.expression(¢));
      case INSTANCEOF_EXPRESSION:
        return free(left(az.instanceofExpression(¢)));
      case PREFIX_EXPRESSION:
        return free(az.prefixExpression(¢));
      case PARENTHESIZED_EXPRESSION:
        return free(core(¢));
      case INFIX_EXPRESSION:
        return free(extract.allOperands(az.infixExpression(¢)));
      case CONDITIONAL_EXPRESSION:
        return free(az.conditionalExpression(¢));
      case ARRAY_INITIALIZER:
        return free(step.expressions(az.arrayInitializer(¢)));
      default:
        throw new RuntimeException("Missing handler for class: " + ¢.getClass().getSimpleName());
    }
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
    return in(x.getOperator(), PLUS, MINUS, COMPLEMENT, NOT) && free(step.operand(x));
  }
}
