package il.org.spartan.spartanizer.java;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
public enum sideEffects {
  MISSING_CASE;
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
    x.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation __) {
        $.clear();
        return false;
      }
    });
    return $.get();
  }

  private static boolean free(final ArrayCreation ¢) {
    return free(dimensions(¢)) && free(expressions(¢.getInitializer()));
  }

  public static boolean free(final ASTNode ¢) {
    return ¢ == null || //
        (iz.expression(¢) ? sideEffects.free(az.expression(¢))
            : iz.expressionStatement(¢) ? sideEffects.free(expression(az.expressionStatement(¢)))
                : iz.isVariableDeclarationStatement(¢) ? free(az.variableDeclrationStatement(¢)) : iz.block(¢) && free(az.block(¢)));
  }

  public static boolean free(final Block ¢) {
    return statements(¢).stream().allMatch(sideEffects::free);
  }

  private static boolean free(final ConditionalExpression ¢) {
    return free(expression(¢), then(¢), elze(¢));
  }

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
        return free(extract.allOperands(az.infixExpression(¢)));
      case PARENTHESIZED_EXPRESSION:
        return free(core(¢));
      case INSTANCEOF_EXPRESSION:
        return free(left(az.instanceofExpression(¢)));
      default:
        monitor.logProbableBug(sideEffects.MISSING_CASE, new AssertionError("Missing 'case' in switch for class: " + ¢.getClass().getSimpleName()));
        return false;
    }
  }

  private static boolean free(final Expression... ¢) {
    return Stream.of(¢).allMatch(sideEffects::free);
  }

  public static boolean free(final Iterable<? extends Expression> ¢) {
    return ¢ == null || az.stream(¢).allMatch(sideEffects::free);
  }

  static boolean free(final List<VariableDeclarationFragment> fs) {
    return fs.stream().map(λ -> initializer(λ)).allMatch(λ -> free(λ));
  }

  public static boolean free(final MethodDeclaration ¢) {
    return sideEffects.free(¢.getBody());
  }

  private static boolean free(final PrefixExpression ¢) {
    return in(¢.getOperator(), PLUS, MINUS, COMPLEMENT, NOT) && free(operand(¢));
  }

  private static boolean free(final VariableDeclarationExpression ¢) {
    return free(fragments(¢));
  }

  public static boolean free(final VariableDeclarationStatement ¢) {
    return free(fragments(¢));
  }
}
