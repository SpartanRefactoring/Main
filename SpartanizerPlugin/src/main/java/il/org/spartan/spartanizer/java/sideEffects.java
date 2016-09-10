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
      BOOLEAN__LITERAL, //
      CHARACTER__LITERAL, //
      EMPTY__STATEMENT, //
      FIELD__ACCESS, //
      NULL__LITERAL, //
      NUMBER__LITERAL, //
      PRIMITIVE__TYPE, //
      QUALIFIED__NAME, //
      SIMPLE__NAME, //
      SIMPLE__TYPE, //
      STRING__LITERAL, //
      SUPER__FIELD__ACCESS, //
      THIS__EXPRESSION, //
      TYPE__LITERAL, //
  };
  private static final int[] alwaysHave = { //
      SUPER__CONSTRUCTOR__INVOCATION, //
      SUPER__METHOD__INVOCATION, //
      METHOD__INVOCATION, //
      CLASS__INSTANCE__CREATION, //
      ASSIGNMENT, //
      POSTFIX__EXPRESSION, //
  };

  public static boolean deterministic(final Expression x) {
    if (!free(x))
      return false;
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.TRUE);
    x.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation ____) {
        $.set(Boolean.FALSE);
        return false;
      }
    });
    return $.get().booleanValue();
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
      case ARRAY__CREATION:
        return free((ArrayCreation) ¢);
      case ARRAY__ACCESS:
        return free(((ArrayAccess) ¢).getArray(), ((ArrayAccess) ¢).getIndex());
      case CAST__EXPRESSION:
        return free(step.expression(¢));
      case INSTANCEOF__EXPRESSION:
        return free(step.left(az.instanceofExpression(¢)));
      case PREFIX__EXPRESSION:
        return free(az.prefixExpression(¢));
      case PARENTHESIZED__EXPRESSION:
        return free(core(¢));
      case INFIX__EXPRESSION:
        return free(extract.allOperands(az.infixExpression(¢)));
      case CONDITIONAL__EXPRESSION:
        return free(az.conditionalExpression(¢));
      case ARRAY__INITIALIZER:
        return free(step.expressions(az.arrayInitializer(¢)));
      default:
        throw new RuntimeException("Missing handler for class: " + ¢.getClass().getSimpleName());
    }
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
    return in(x.getOperator(), PLUS, MINUS, COMPLEMENT, NOT) && free(step.operand(x));
  }
}
