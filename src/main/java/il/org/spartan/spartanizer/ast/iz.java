package il.org.spartan.spartanizer.ast;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum iz {
  ;
  public static boolean __abstract(final BodyDeclaration ¢) {
    return (¢.getModifiers() & Modifier.ABSTRACT) != 0;
  }

  public static boolean __final(final BodyDeclaration ¢) {
    return (Modifier.FINAL & ¢.getModifiers()) != 0;
  }

  /** Determine whether a variable declaration is final or not
   * @param subject some declaration
   * @return <code><b>true</b></code> <i>iff</i> the variable is declared as
   *         final */
  public static boolean __final(final VariableDeclarationStatement ¢) {
    return (Modifier.FINAL & ¢.getModifiers()) != 0;
  }

  public static boolean abstractTypeDeclaration(final ASTNode ¢) {
    return ¢ != null && ¢ instanceof AbstractTypeDeclaration;
  }

  public static boolean annotation(final IExtendedModifier ¢) {
    return ¢ instanceof Annotation;
  }

  public static boolean anonymousClassDeclaration(final ASTNode ¢) {
    return is(¢, ANONYMOUS_CLASS_DECLARATION);
  }

  public static boolean arrayInitializer(final ASTNode ¢) {
    return is(¢, ARRAY_INITIALIZER);
  }

  /** @param n the statement or block to check if it is an assignment
   * @return <code><b>true</b></code> if the parameter an assignment or false if
   *         the parameter not or if the block Contains more than one
   *         statement */
  public static boolean assignment(final ASTNode ¢) {
    return is(¢, ASSIGNMENT);
  }

  public static boolean astNode(final Object ¢) {
    return ¢ != null && ¢ instanceof ASTNode;
  }

  /** Determine whether a node is a {@link Block}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean block(final ASTNode ¢) {
    return is(¢, BLOCK);
  }

  /** @param subject JD
   * @return true if the parameter is an essential block or false otherwise */
  public static boolean blockEssential(final Statement ¢) {
    return blockEssential(az.ifStatement(¢));
  }

  /** @param subject JD
   * @return */
  public static boolean blockRequired(final IfStatement ¢) {
    return blockRequiredInReplacement(¢, ¢);
  }

  public static boolean blockRequired(final Statement s) {
    final IfStatement s1 = az.ifStatement(s);
    return blockRequiredInReplacement(s1, s1);
  }

  public static boolean blockRequiredInReplacement(final IfStatement old, final IfStatement newIf) {
    if (newIf == null || old != newIf && elze(old) == null == (elze(newIf) == null))
      return false;
    final IfStatement parent = az.ifStatement(step.parent(old));
    return parent != null && then(parent) == old && (elze(parent) == null || elze(newIf) == null)
        && (elze(parent) != null || elze(newIf) != null || blockRequiredInReplacement(parent, newIf));
  }

  /** Determine whether a node is a boolean literal
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a boolean
   *         literal */
  public static boolean booleanLiteral(final ASTNode ¢) {
    return is(¢, BOOLEAN_LITERAL);
  }

  /** @param ¢ node to check
   * @return true if the given node is a boolean or null literal or false
   *         otherwise */
  public static boolean booleanOrNullLiteral(final ASTNode ¢) {
    return is(¢, BOOLEAN_LITERAL, NULL_LITERAL);
  }

  public static boolean breakStatement(final Statement ¢) {
    return is(¢, BREAK_STATEMENT);
  }

  /** @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a comparison
   *         expression. */
  public static boolean comparison(final InfixExpression ¢) {
    return in(¢.getOperator(), EQUALS, GREATER, GREATER_EQUALS, LESS, LESS_EQUALS, NOT_EQUALS);
  }

  public static boolean comparison(final Operator ¢) {
    return in(¢, EQUALS, NOT_EQUALS, GREATER_EQUALS, GREATER, LESS, LESS_EQUALS);
  }

  /** @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one of the parameters is a
   *         conditional or parenthesized conditional expression */
  public static boolean conditional(final Expression... xs) {
    for (final Expression ¢ : xs)
      if (is(extract.core(¢), CONDITIONAL_EXPRESSION))
        return true;
    return false;
  }

  /** Check whether an expression is a "conditional and" (&&)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_AND} */
  public static boolean conditionalAnd(final InfixExpression ¢) {
    return ¢.getOperator() == CONDITIONAL_AND;
  }

  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR} */
  public static boolean conditionalOr(final Expression ¢) {
    return conditionalOr(az.infixExpression(¢));
  }

  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR} */
  public static boolean conditionalOr(final InfixExpression ¢) {
    return ¢ != null && ¢.getOperator() == CONDITIONAL_OR;
  }

  /** Determine whether a node is a "specific", i.e., <code><b>null</b></code>
   * or <code><b>this</b></code> or literal.
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a
   *         "specific" */
  public static boolean constant(final Expression ¢) {
    return is(¢, CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION)
        || is(¢, PREFIX_EXPRESSION) && iz.constant(extract.core(((PrefixExpression) ¢).getOperand()));
  }

  /** Check whether the operator of an expression is susceptible for applying
   * one of the two de Morgan laws.
   * @param x InfixExpression
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply. */
  public static boolean deMorgan(final InfixExpression ¢) {
    return iz.deMorgan(¢.getOperator());
  }

  /** Check whether an operator is susceptible for applying one of the two de
   * Morgan laws.
   * @param o JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply. */
  public static boolean deMorgan(final Operator ¢) {
    return in(¢, CONDITIONAL_AND, CONDITIONAL_OR);
  }

  public static boolean doubleType(final Expression ¢) {
    return type.get(¢) == DOUBLE;
  }

  /** Determine whether a node is an {@link EmptyStatement}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link EmptyStatement} */
  public static boolean emptyStatement(final ASTNode ¢) {
    return is(¢, EMPTY_STATEMENT);
  }

  public static boolean emptyStringLiteral(final ASTNode ¢) {
    return iz.literal("", ¢);
  }

  public static boolean enumConstantDeclaration(final ASTNode ¢) {
    return is(¢, ENUM_CONSTANT_DECLARATION);
  }

  public static boolean enumDeclaration(final ASTNode ¢) {
    return is(¢, ENUM_DECLARATION);
  }

  /** Determine whether a node is an "expression statement"
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link ExpressionStatement} statement */
  public static boolean expression(final ASTNode ¢) {
    return ¢ != null && ¢ instanceof Expression;
  }

  /** Determine whether a node is an "expression statement"
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link ExpressionStatement} statement */
  public static boolean expressionStatement(final ASTNode ¢) {
    return is(¢, EXPRESSION_STATEMENT);
  }

  /** @param o The operator to check
   * @return True - if the operator have opposite one in terms of operands
   *         swap. */
  public static boolean flipable(final Operator ¢) {
    return in(¢, //
        AND, //
        EQUALS, //
        GREATER, //
        GREATER_EQUALS, //
        LESS_EQUALS, //
        LESS, //
        NOT_EQUALS, //
        OR, //
        PLUS, // Too risky
        TIMES, //
        XOR, //
        null);
  }

  /** @param n the statement or block to check if it is an for statement
   * @return <code><b>true</b></code> if the parameter an for statement or false
   *         if the parameter not or if the block Contains more than one
   *         statement */
  public static boolean forStatement(final ASTNode ¢) {
    return is(¢, FOR_STATEMENT);
  }

  public static boolean identifier(final String identifier, final SimpleName n) {
    return identifier.equals(n.getIdentifier());
  }

  public static boolean ifStatement(final Statement ¢) {
    return is(¢, IF_STATEMENT);
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the node is an Expression
   *         Statement of type Post or Pre Expression with ++ or -- operator
   *         false if node is not an Expression Statement or its a Post or Pre
   *         fix expression that its operator is not ++ or -- */
  public static boolean incrementOrDecrement(final ASTNode ¢) {
    switch (¢.getNodeType()) {
      case EXPRESSION_STATEMENT:
        return incrementOrDecrement(step.expression(¢));
      case POSTFIX_EXPRESSION:
        return in(((PostfixExpression) ¢).getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
      case PREFIX_EXPRESSION:
        return in(az.prefixExpression(¢).getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
      default:
        return false;
    }
  }

  public static int index(final int i, final int... is) {
    for (int $ = 0; $ < is.length; ++$)
      if (is[$] == i)
        return $;
    return -1;
  }

  /** @param ¢ JD
   * @return true if the given node is an infix expression or false otherwise */
  public static boolean infix(final ASTNode ¢) {
    return is(¢, INFIX_EXPRESSION);
  }

  public static boolean infixDivide(final Expression ¢) {
    return step.operator(az.infixExpression(¢)) == DIVIDE;
  }

  public static boolean infixExpression(final ASTNode ¢) {
    return is(¢, INFIX_EXPRESSION);
  }

  public static boolean infixMinus(final ASTNode ¢) {
    return step.operator(az.infixExpression(¢)) == wizard.MINUS2;
  }

  public static boolean infixPlus(final ASTNode ¢) {
    return step.operator(az.infixExpression(¢)) == wizard.PLUS2;
  }

  public static boolean infixTimes(final Expression ¢) {
    return step.operator(az.infixExpression(¢)) == TIMES;
  }

  public static boolean intType(final Expression ¢) {
    return type.get(¢) == INT;
  }

  public static boolean is(final ASTNode ¢, final int... types) {
    return ¢ != null && intIsIn(¢.getNodeType(), types);
  }

  /** Determine whether a declaration is final or not
   * @param ¢ JD
   * @return true if declaration is final */
  public static boolean isFinal(final BodyDeclaration ¢) {
    return (Modifier.FINAL & ¢.getModifiers()) != 0;
  }

  /** Determine whether a variable declaration is final or not
   * @param ¢ JD
   * @return true if the variable is declared as final */
  public static boolean isFinal(final VariableDeclarationStatement ¢) {
    return (Modifier.FINAL & ¢.getModifiers()) != 0;
  }

  /** @param ¢ JD
   * @return true if the given node is an interface or false otherwise */
  public static boolean isInterface(final ASTNode ¢) {
    return is(¢, TYPE_DECLARATION) && ((TypeDeclaration) ¢).isInterface();
  }

  /** @param ¢ JD
   * @return true if the given node is a method decleration or false
   *         otherwise */
  public static boolean isMethodDeclaration(final ASTNode ¢) {
    return is(¢, METHOD_DECLARATION);
  }

  /** @param ¢ node to check
   * @return true if the given node is a method invocation or false otherwise */
  public static boolean isMethodInvocation(final ASTNode ¢) {
    return is(¢, METHOD_INVOCATION);
  }

  /** @param a the assignment whose operator we want to check
   * @return true is the assignment'¢ operator is plus assign */
  public static boolean isMinusAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == MINUS_ASSIGN;
  }

  /** @param a the assignment whose operator we want to check
   * @return true is the assignment'¢ operator is assign */
  public static boolean isPlainAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == ASSIGN;
  }

  /** @param a the assignment whose operator we want to check
   * @return true is the assignment'¢ operator is plus assign */
  public static boolean isPlusAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == PLUS_ASSIGN;
  }

  /** Determine whether a declaration is private
   * @param ¢ JD
   * @return true if declaration is private */
  public static boolean isPrivate(final BodyDeclaration ¢) {
    return (Modifier.PRIVATE & ¢.getModifiers()) != 0;
  }

  /** Determine whether a declaration is static or not
   * @param ¢ JD
   * @return true if declaration is static */
  public static boolean isStatic(final BodyDeclaration ¢) {
    return (Modifier.STATIC & ¢.getModifiers()) != 0;
  }

  /** @param ¢ node to check
   * @return true if the given node is a variable declaration statement or false
   *         otherwise */
  public static boolean isVariableDeclarationStatement(final ASTNode ¢) {
    return is(¢, VARIABLE_DECLARATION_STATEMENT);
  }

  /** Determine whether an item is the last one in a list
   * @param t a list item
   * @param ts a list
   * @return <code><b>true</b></code> <i>iff</i> the item is found in the list
   *         and it is the last one in it. */
  public static <T> boolean last(final T t, final List<T> ts) {
    return ts.indexOf(t) == ts.size() - 1;
  }

  /** @param n Expression node
   * @return <code><b>true</b></code> <i>iff</i> the Expression is literal */
  public static boolean literal(final ASTNode ¢) {
    return ¢ != null && intIsIn(¢.getNodeType(), NULL_LITERAL, CHARACTER_LITERAL, NUMBER_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL);
  }

  public static boolean literal(final ASTNode ¢, final double d) {
    final NumberLiteral ¢1 = az.numberLiteral(¢);
    return ¢1 != null && literal(¢1.getToken(), d);
  }

  /** @param subject JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter return a
   *         literal */
  public static boolean literal(final ReturnStatement ¢) {
    return literal(¢.getExpression());
  }

  public static boolean literal(final String s, final ASTNode ¢) {
    return literal(az.stringLiteral(¢), s);
  }

  /** @param ¢ JD
   * @return true if the given node is a literal or false otherwise */
  public static boolean literal(final String token, final double d) {
    try {
      return Double.parseDouble(token) == d;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException ____) {
      return false;
    }
  }

  /** @param ¢ JD
   * @return true if the given node is a literal 0 or false otherwise */
  public static boolean literal0(final ASTNode ¢) {
    return iz.literal(¢, 0);
  }

  /** @param ¢ JD
   * @return true if the given node is a literal 1 or false otherwise */
  public static boolean literal1(final ASTNode ¢) {
    return iz.literal(¢, 1);
  }

  /** @param ¢ JD
   * @return true if the given node is a literal false or false otherwise */
  public static boolean literalFalse(final ASTNode ¢) {
    return iz.literal(¢, false);
  }

  /** @param ¢ JD
   * @return true if the given node is a literal true or false otherwise */
  public static boolean literalTrue(final ASTNode ¢) {
    return iz.literal(¢, true);
  }

  /** @param ¢ JD
   * @return true if the given node is a literal 0 or false otherwise */
  public static boolean literalZero(final ASTNode ¢) {
    return iz.literal(¢, 0);
  }

  public static boolean longType(final Expression ¢) {
    return type.get(¢) == LONG;
  }

  /** Determine whether a node is a {@link MethodDeclaration}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation. */
  public static boolean methodDeclaration(final ASTNode ¢) {
    return is(¢, METHOD_DECLARATION);
  }

  /** Determine whether a node is a {@link MethodInvocation}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation. */
  public static boolean methodInvocation(final ASTNode ¢) {
    return is(¢, METHOD_INVOCATION);
  }

  public static boolean modifier(final ASTNode ¢) {
    return is(¢, MODIFIER);
  }

  public static boolean negative(final Expression ¢) {
    return negative(az.prefixExpression(¢)) || negative(az.numberLiteral(¢));
  }

  public static boolean negative(final NumberLiteral ¢) {
    return ¢ != null && ¢.getToken().startsWith("-");
  }

  public static boolean negative(final PrefixExpression ¢) {
    return ¢ != null && ¢.getOperator() == PrefixExpression.Operator.MINUS;
  }

  /** Determine whether an {@link Expression} is so basic that it never needs to
   * be placed in parenthesis.
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is so basic that
   *         it never needs to be placed in parenthesis. */
  public static boolean noParenthesisRequired(final Expression ¢) {
    return in(¢.getClass(), //
        BooleanLiteral.class, //
        CharacterLiteral.class, //
        ClassInstanceCreation.class, //
        FieldAccess.class, //
        MethodInvocation.class, //
        Name.class, //
        NullLiteral.class, //
        NumberLiteral.class, //
        ParenthesizedExpression.class, //
        QualifiedName.class, //
        SimpleName.class, //
        StringLiteral.class, //
        SuperFieldAccess.class, //
        SuperMethodInvocation.class, //
        ThisExpression.class, //
        TypeLiteral.class, //
        null);
  }

  public static boolean normalAnnotations(final ASTNode ¢) {
    return is(¢, NORMAL_ANNOTATION);
  }

  /** Determine whether a node is the <code><b>null</b></code> keyword
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i>is thee <code><b>null</b></code>
   *         literal */
  public static boolean nullLiteral(final ASTNode ¢) {
    return is(¢, NULL_LITERAL);
  }

  public static boolean number(final Expression ¢) {
    return iz.numberLiteral(¢) && //
        (type.isInt(¢) || type.isDouble(¢) || type.isLong(¢));
  }

  public static boolean numberLiteral(final ASTNode ¢) {
    return is(¢, NUMBER_LITERAL);
  }

  /** Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean numericLiteral(final Expression ¢) {
    return iz.oneOf(¢, CHARACTER_LITERAL, NUMBER_LITERAL);
  }

  /** Determine whether the type of an {@link ASTNode} node is one of given list
   * @param n a node
   * @param types a list of types
   * @return <code><b>true</b></code> <i>iff</i> function #ASTNode.getNodeType
   *         returns one of the types provided as parameters */
  public static boolean oneOf(final ASTNode n, final int... types) {
    return n != null && isOneOf(n.getNodeType(), types);
  }

  public static boolean parenthesizeExpression(final Expression ¢) {
    return is(¢, PARENTHESIZED_EXPRESSION);
  }

  /** @param a the assignment who's operator we want to check
   * @return true is the assignment's operator is assign */
  public static boolean plainAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == ASSIGN;
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a prefix
   *         expression. */
  public static boolean prefixExpression(final ASTNode ¢) {
    return is(¢, PREFIX_EXPRESSION);
  }

  public static boolean pseudoNumber(final Expression ¢) {
    return number(¢) || iz.prefixMinus(¢) && iz.number(az.prefixExpression(¢).getOperand());
  }

  /** Determine whether a node is a return statement
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a return
   *         statement. */
  public static boolean returnStatement(final ASTNode ¢) {
    return is(¢, RETURN_STATEMENT);
  }

  /** Determine whether a node is a "sequencer", i.e.,
   * <code><b>return</b></code> , <code><b>break</b></code>,
   * <code><b>continue</b></code> or <code><b>throw</b></code>
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a sequencer */
  public static boolean sequencer(final ASTNode ¢) {
    return iz.oneOf(¢, RETURN_STATEMENT, BREAK_STATEMENT, CONTINUE_STATEMENT, THROW_STATEMENT);
  }

  /** Checks if expression is simple.
   * @param x an expression
   * @return true iff argument is simple */
  public static boolean simple(final Expression ¢) {
    return is(¢, //
        BOOLEAN_LITERAL, //
        CHARACTER_LITERAL, //
        NULL_LITERAL, //
        NUMBER_LITERAL, //
        QUALIFIED_NAME, //
        SIMPLE_NAME, //
        STRING_LITERAL, //
        THIS_EXPRESSION, //
        TYPE_LITERAL //
    );
  }

  /** Determine whether a node is a simple name
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a simple
   *         name */
  public static boolean simpleName(final ASTNode ¢) {
    return is(¢, SIMPLE_NAME);
  }

  public static boolean singleMemberAnnotation(final ASTNode ¢) {
    return is(¢, SINGLE_MEMBER_ANNOTATION);
  }

  /** Determine whether a node is a singleton statement, i.e., not a block.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a singleton
   *         statement. */
  public static boolean singletonStatement(final ASTNode ¢) {
    return extract.statements(¢).size() == 1;
  }

  /** Determine whether the "then" branch of an {@link Statement} is a single
   * statement.
   * @param subject JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a statement */
  public static boolean singletonThen(final IfStatement ¢) {
    return iz.singletonStatement(then(¢));
  }

  /** @param ¢ JD
   * @return true if the given node is a statement or false otherwise */
  public static boolean statement(final ASTNode ¢) {
    return ¢ instanceof Statement;
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a string
   *         literal */
  public static boolean stringLiteral(final ASTNode ¢) {
    return ¢ != null && ¢.getNodeType() == STRING_LITERAL;
  }

  /** Determine whether a node is the <code><b>this</b></code> keyword
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> is the <code><b>this</b></code>
   *         keyword */
  public static boolean thisLiteral(final ASTNode ¢) {
    return is(¢, THIS_EXPRESSION);
  }

  /** Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean thisOrNull(final Expression ¢) {
    return iz.oneOf(¢, NULL_LITERAL, THIS_EXPRESSION);
  }

  /** Determine whether a given {@link Statement} is an {@link EmptyStatement}
   * or has nothing but empty statements in it.
   * @param subject JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter */
  public static boolean vacuous(final Statement ¢) {
    return extract.statements(¢).isEmpty();
  }

  /** Determine whether the 'else' part of an {@link IfStatement} is vacuous.
   * @param subject JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the 'else' part of the parameter */
  public static boolean vacuousElse(final IfStatement ¢) {
    return vacuous(elze(¢));
  }

  /** Determine whether a statement is an {@link EmptyStatement} or has nothing
   * but empty statements in it.
   * @param subject JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter */
  public static boolean vacuousThen(final IfStatement ¢) {
    return vacuous(then(¢));
  }

  public static boolean validForEvaluation(final InfixExpression x) {
    final List<Expression> lst = extract.allOperands(x);
    for (final Expression ¢ : lst)
      if (!iz.pseudoNumber(¢))
        return false;
    return true;
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a variable
   *         declaration statement. */
  public static boolean variableDeclarationStatement(final ASTNode ¢) {
    return is(¢, VARIABLE_DECLARATION_STATEMENT);
  }

  public static boolean whileStatement(final ASTNode x) {
    return x instanceof WhileStatement;
  }

  public static boolean wildcardType(final ASTNode ¢) {
    return is(¢, WILDCARD_TYPE);
  }

  /** Determine whether the curly brackets of an {@link IfStatement} are
   * vacuous.
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> the curly brackets are
   *         essential */
  static boolean blockEssential(final IfStatement s) {
    if (s == null)
      return false;
    final Block b = az.block(step.parent(s));
    if (b == null)
      return false;
    final IfStatement parent = az.ifStatement(step.parent(b));
    return parent != null && (elze(parent) == null || wizard.recursiveElze(s) == null)
        && (elze(parent) != null || wizard.recursiveElze(s) != null || blockRequiredInReplacement(parent, s));
  }

  static boolean literal(final ASTNode ¢, final boolean b) {
    return literal(az.booleanLiteral(¢), b);
  }

  static boolean literal(final ASTNode ¢, final int i) {
    final NumberLiteral ¢1 = az.numberLiteral(¢);
    return ¢1 != null && literal(¢1.getToken(), i);
  }

  static boolean literal(final ASTNode ¢, final long l) {
    return literal(az.numberLiteral(¢).getToken(), l);
  }

  static boolean literal(final BooleanLiteral ¢, final boolean b) {
    return ¢ != null && ¢.booleanValue() == b;
  }

  static boolean literal(final String token, final int i) {
    try {
      return Integer.parseInt(token) == i;
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return false;
    }
  }

  static boolean literal(final String token, final long l) {
    try {
      return Long.parseLong(token) == l;
    } catch (final IllegalArgumentException x) {
      Plugin.log(x);
      return false;
    }
  }

  static boolean literal(final StringLiteral ¢, final String s) {
    return ¢ != null && ¢.getLiteralValue().equals(s);
  }

  private static boolean is(final ASTNode n, final int type) {
    return n != null && type == n.getNodeType();
  }

  private static boolean isOneOf(final int i, final int... is) {
    for (final int j : is)
      if (i == j)
        return true;
    return false;
  }

  private static boolean prefixMinus(final Expression ¢) {
    return iz.prefixExpression(¢) && az.prefixExpression(¢).getOperator() == wizard.MINUS1;
  }
}