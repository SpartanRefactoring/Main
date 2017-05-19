package il.org.spartan.spartanizer.java;

import static fluent.ly.is.*;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
public enum sideEffects {
  MISSING_CASE;
  /** Determine whether the evaluation of an expression is guaranteed to be free
   * of any side effects.
   * @param e JD
   * @return whether the parameter is an expression whose computation is
   *         guaranteed to be free of any side effects. */
  // VIM: /{/+,/}/-!sort -u
  private static final int[] alwaysFree = { //
      BOOLEAN_LITERAL, //
      CHARACTER_LITERAL, //
      EMPTY_STATEMENT, //
      FIELD_ACCESS, //
      LAMBDA_EXPRESSION, //
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
      EXPRESSION_METHOD_REFERENCE, //
      CREATION_REFERENCE, //
      SUPER_METHOD_REFERENCE, //
      TYPE_METHOD_REFERENCE, //
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
    if (!sideEffects.free(x))
      return false;
    final Bool $ = new Bool(true);
    // noinspection SameReturnValue
    x.accept(new ASTVisitor(true) {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation __) {
        $.clear();
        return false;
      }
    });
    return $.get();
  }
  public static boolean free(final IfStatement ¢) {
    return free(¢.getExpression()) && free(then(¢)) && free(elze(¢));
  }
  public static boolean free(final ExpressionStatement ¢) {
    return free(¢.getExpression());
  }
  private static boolean free(final ArrayCreation ¢) {
    return free(dimensions(¢)) && free(expressions(¢.getInitializer()));
  }
  public static boolean free(final ASTNode ¢) {
    return ¢ == null || (iz.expression(¢) ? free(az.expression(¢))
        : iz.expressionStatement(¢) ? free(az.expressionStatement(¢))
            : iz.ifStatement(¢) ? free(az.ifStatement(¢))
                : iz.whileStatement(¢) ? free(az.whileStatement(¢))
                    : iz.forStatement(¢) ? free(az.forStatement(¢))
                        : iz.isVariableDeclarationStatement(¢) ? free(az.variableDeclrationStatement(¢)) : iz.block(¢) && free(az.block(¢)));
  }
  public static boolean free(final ForStatement ¢) {
    return free(initializers(¢)) && free(¢.getExpression()) && free(updaters(¢)) && free(body(¢));
  }
  public static boolean free(final Block ¢) {
    return statements(¢).stream().allMatch(sideEffects::free);
  }
  private static boolean free(final ConditionalExpression ¢) {
    return free(expression(¢), then(¢), elze(¢));
  }
  public static boolean sink(final Expression x) {
    return descendants.of(x).stream().mapToInt(λ -> λ.getNodeType()).noneMatch(λ -> is.intIsIn(λ, STRICT_SIDE_EFFECT));
  }

  static final int[] STRICT_SIDE_EFFECT = { METHOD_INVOCATION, SUPER_CONSTRUCTOR_INVOCATION, CONSTRUCTOR_INVOCATION, CLASS_INSTANCE_CREATION,
      ASSIGNMENT, POSTFIX_EXPRESSION };

  public static boolean free(final Expression ¢) {
    if (¢ == null || iz.nodeTypeIn(¢, alwaysFree))
      return true;
    if (iz.nodeTypeIn(¢, alwaysHave))
      return false;
    switch (¢.getNodeType()) {
      case CONDITIONAL_EXPRESSION:
        return free(az.conditionalExpression(¢));
      case PREFIX_EXPRESSION:
        return free(az.prefixExpression(¢));
      case VARIABLE_DECLARATION_EXPRESSION:
        return free(az.variableDeclarationExpression(¢));
      case ARRAY_CREATION:
        return free((ArrayCreation) ¢);
      case ARRAY_INITIALIZER:
        return free(expressions(az.arrayInitializer(¢)));
      case ARRAY_ACCESS:
        return free(((ArrayAccess) ¢).getArray(), ((ArrayAccess) ¢).getIndex());
      case CAST_EXPRESSION:
        return sideEffects.free(expression(¢));
      case INFIX_EXPRESSION:
        return free(allOperands(az.infixExpression(¢)));
      case PARENTHESIZED_EXPRESSION:
        return sideEffects.free(core(¢));
      case INSTANCEOF_EXPRESSION:
        return sideEffects.free(left(az.instanceofExpression(¢)));
      default:
        note.bug(sideEffects.MISSING_CASE, new AssertionError("Missing 'case' in switch for class: " + wizard.nodeName(¢)));
        return false;
    }
  }
  private static boolean free(final Expression... ¢) {
    return Stream.of(¢).allMatch(sideEffects::free);
  }
  public static boolean free(final Iterable<? extends Expression> xs) {
    return xs == null || az.stream(xs).allMatch(λ -> sideEffects.free(az.expression(λ)));
  }
  public static boolean free(final MethodDeclaration ¢) {
    return sideEffects.free(¢.getBody());
  }
  private static boolean free(final PrefixExpression ¢) {
    return in(¢.getOperator(), PLUS, MINUS, COMPLEMENT, NOT) && sideEffects.free(operand(¢));
  }
  private static boolean free(final VariableDeclarationExpression x) {
    return fragments(x).stream().allMatch(λ -> sideEffects.free(initializer(λ)));
  }
  public static boolean free(final VariableDeclarationStatement s) {
    return fragments(s).stream().allMatch(λ -> sideEffects.free(initializer(λ)));
  }
  public static boolean free(final WhileStatement ¢) {
    return free(¢.getExpression()) && free(¢.getBody());
  }
}
