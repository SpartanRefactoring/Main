package il.org.spartan.spartanizer.java;

import static fluent.ly.is.in;
import static il.org.spartan.spartanizer.ast.navigate.extract.allOperands;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.dimensions;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.expressions;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.initializers;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.operand;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BOOLEAN_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CAST_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CHARACTER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CLASS_INSTANCE_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONDITIONAL_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.CREATION_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_METHOD_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.INSTANCEOF_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.POSTFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PREFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PRIMITIVE_TYPE;
import static org.eclipse.jdt.core.dom.ASTNode.QUALIFIED_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.SIMPLE_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.SIMPLE_TYPE;
import static org.eclipse.jdt.core.dom.ASTNode.STRING_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.THIS_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_METHOD_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_EXPRESSION;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.COMPLEMENT;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.NOT;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.is;
import fluent.ly.note;
import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.Bool;

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
    return descendants.of(x).stream().mapToInt(ASTNode::getNodeType).noneMatch(λ -> is.intIsIn(λ, STRICT_SIDE_EFFECT));
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
