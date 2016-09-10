package il.org.spartan.spartanizer.ast;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum iz {
  ;
  /** Determine whether a variable declaration is final or not
   * @param s some declaration
   * @return <code><b>true</b></code> <i>iff</i> the variable is declared as
   *         final */
  public static boolean __final(final VariableDeclarationStatement s) {
    return (Modifier.FINAL & s.getModifiers()) != 0;
  }

  public static boolean abstractTypeDeclaration(final ASTNode ¢) {
    return ¢ != null && ¢ instanceof AbstractTypeDeclaration;
  }

  public static boolean annotation(final IExtendedModifier ¢) {
    return ¢ instanceof Annotation;
  }

  /* public static boolean modifier(final IExtendedModifier ¢) { return ¢
   * instanceof Annotation; } */
  public static boolean anonymousClassDeclaration(final ASTNode ¢) {
    return is(¢, ANONYMOUS_CLASS_DECLARATION);
  }

  /** @param n the statement or block to check if it is an assignment
   * @return <code><b>true</b></code> if the parameter an assignment or false if
   *         the parameter not or if the block Contains more than one
   *         statement */
  public static boolean assignment(final ASTNode n) {
    return is(n, ASSIGNMENT);
  }

  public static boolean astNode(final Object ¢) {
    return ¢ != null && ¢ instanceof ASTNode;
  }

  /** Determine whether a node is a {@link Block}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean block(final ASTNode n) {
    return is(n, BLOCK);
  }

  /** @param s JD
   * @return true if the parameter is an essential block or false otherwise */
  public static boolean blockEssential(final Statement s) {
    return blockEssential(az.ifStatement(s));
  }

  /** @param s JD
   * @return */
  public static boolean blockRequired(final IfStatement s) {
    return blockRequiredInReplacement(s, s);
  }

  public static boolean blockRequired(final Statement s) {
    final IfStatement s1 = az.ifStatement(s);
    return blockRequiredInReplacement(s1, s1);
  }

  public static boolean blockRequiredInReplacement(final IfStatement old, final IfStatement newIf) {
    if (newIf == null || old != newIf && step.elze(old) == null == (step.elze(newIf) == null))
      return false;
    final IfStatement parent = az.ifStatement(step.parent(old));
    return parent != null && step.then(parent) == old && (step.elze(parent) == null || step.elze(newIf) == null)
        && (step.elze(parent) != null || step.elze(newIf) != null || blockRequiredInReplacement(parent, newIf));
  }

  /** Determine whether a node is a boolean literal
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a boolean
   *         literal */
  public static boolean booleanLiteral(final ASTNode n) {
    return is(n, BOOLEAN_LITERAL);
  }

  /** @param ¢ node to check
   * @return true if the given node is a boolean or null literal or false
   *         otherwise */
  public static boolean booleanOrNullLiteral(final ASTNode ¢) {
    return is(¢, BOOLEAN_LITERAL, NULL_LITERAL);
  }

  public static boolean breakStatement(final Statement s) {
    return is(s, BREAK_STATEMENT);
  }

  /** @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a comparison
   *         expression. */
  public static boolean comparison(final InfixExpression x) {
    return in(x.getOperator(), EQUALS, GREATER, GREATER_EQUALS, LESS, LESS_EQUALS, NOT_EQUALS);
  }

  public static boolean comparison(final Operator o) {
    return in(o, EQUALS, NOT_EQUALS, GREATER_EQUALS, GREATER, LESS, LESS_EQUALS);
  }

  /** @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one of the parameters is a
   *         conditional or parenthesized conditional expression */
  public static boolean conditional(final Expression... xs) {
    for (final Expression e : xs)
      if (is(extract.core(e), CONDITIONAL_EXPRESSION))
        return true;
    return false;
  }

  /** Check whether an expression is a "conditional and" (&&)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_AND} */
  public static boolean conditionalAnd(final InfixExpression x) {
    return x.getOperator() == CONDITIONAL_AND;
  }

  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR} */
  public static boolean conditionalOr(final Expression x) {
    return conditionalOr(az.infixExpression(x));
  }

  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR} */
  public static boolean conditionalOr(final InfixExpression x) {
    return x != null && x.getOperator() == CONDITIONAL_OR;
  }

  /** Determine whether a node is a "specific", i.e., <code><b>null</b></code>
   * or <code><b>this</b></code> or literal.
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a
   *         "specific" */
  public static boolean constant(final Expression x) {
    return is(x, CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION)
        || is(x, PREFIX_EXPRESSION) && iz.constant(extract.core(((PrefixExpression) x).getOperand()));
  }

  /** Check whether the operator of an expression is susceptible for applying
   * one of the two de Morgan laws.
   * @param x InfixExpression
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply. */
  public static boolean deMorgan(final InfixExpression x) {
    return iz.deMorgan(x.getOperator());
  }

  /** Check whether an operator is susceptible for applying one of the two de
   * Morgan laws.
   * @param o JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply. */
  public static boolean deMorgan(final Operator o) {
    return in(o, CONDITIONAL_AND, CONDITIONAL_OR);
  }

  /** Determine whether a node is an {@link EmptyStatement}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link EmptyStatement} */
  public static boolean emptyStatement(final ASTNode n) {
    return is(n, EMPTY_STATEMENT);
  }

  public static boolean emptyStringLiteral(final ASTNode ¢) {
    return iz.literal(¢, "");
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
  public static boolean expression(final ASTNode n) {
    return n != null && n instanceof Expression;
  }

  /** Determine whether a node is an "expression statement"
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link ExpressionStatement} statement */
  public static boolean expressionStatement(final ASTNode n) {
    return is(n, EXPRESSION_STATEMENT);
  }

  /** @param o The operator to check
   * @return True - if the operator have opposite one in terms of operands
   *         swap. */
  public static boolean flipable(final Operator o) {
    return in(o, //
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

  public static boolean ifStatement(final Statement s) {
    return is(s, IF_STATEMENT);
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the node is an Expression
   *         Statement of type Post or Pre Expression with ++ or -- operator
   *         false if node is not an Expression Statement or its a Post or Pre
   *         fix expression that its operator is not ++ or -- */
  public static boolean incrementOrDecrement(final ASTNode n) {
    switch (n.getNodeType()) {
      case EXPRESSION_STATEMENT:
        return incrementOrDecrement(step.expression(n));
      case POSTFIX_EXPRESSION:
        return in(((PostfixExpression) n).getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
      case PREFIX_EXPRESSION:
        return in(az.prefixExpression(n).getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
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

  public static boolean infixDivide(final Expression x) {
    return step.operator(az.infixExpression(x)) == DIVIDE;
  }

  public static boolean infixExpression(final ASTNode n) {
    return is(n, INFIX_EXPRESSION);
  }

  public static boolean infixMinus(final Expression x) {
    return step.operator(az.infixExpression(x)) == wizard.MINUS2;
  }

  public static boolean infixPlus(final Expression x) {
    return step.operator(az.infixExpression(x)) == wizard.PLUS2;
  }

  public static boolean infixTimes(final Expression x) {
    return step.operator(az.infixExpression(x)) == TIMES;
  }

  public static boolean is(final ASTNode ¢, final int... types) {
    return ¢ != null && lisp.intIsIn(¢.getNodeType(), types);
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

  /** @param a the assignment who'¢ operator we want to check
   * @return true is the assignment'¢ operator is assign */
  public static boolean isOpAssign(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.ASSIGN;
  }

  /** @param a the assignment who'¢ operator we want to check
   * @return true is the assignment'¢ operator is plus assign */
  public static boolean isOpMinusAssign(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.MINUS_ASSIGN;
  }

  /** @param a the assignment who'¢ operator we want to check
   * @return true is the assignment'¢ operator is plus assign */
  public static boolean isOpPlusAssign(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.PLUS_ASSIGN;
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
  public static boolean literal(final ASTNode n) {
    return n != null && lisp.intIsIn(n.getNodeType(), NULL_LITERAL, CHARACTER_LITERAL, NUMBER_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL);
  }

  public static boolean literal(final ASTNode ¢, final double d) {
    final NumberLiteral ¢1 = az.numberLiteral(¢);
    return ¢1 != null && literal(¢1.getToken(), d);
  }

  /** @param s JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter return a
   *         literal */
  public static boolean literal(final ReturnStatement s) {
    return literal(s.getExpression());
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

  /** Determine whether a node is a {@link MethodDeclaration}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation. */
  public static boolean methodDeclaration(final ASTNode n) {
    return is(n, METHOD_DECLARATION);
  }

  /** Determine whether a node is a {@link MethodInvocation}
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation. */
  public static boolean methodInvocation(final ASTNode n) {
    return is(n, METHOD_INVOCATION);
  }

  public static boolean modifier(final ASTNode ¢) {
    return is(¢, MODIFIER);
  }

  public static boolean negative(final Expression x) {
    return negative(az.prefixExpression(x)) || negative(az.numberLiteral(x));
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
  public static boolean noParenthesisRequired(final Expression x) {
    return in(x.getClass(), //
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

  /** Determine whether a node is the <code><b>null</b></code> keyword
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i>is thee <code><b>null</b></code>
   *         literal */
  public static boolean nullLiteral(final ASTNode n) {
    return is(n, NULL_LITERAL);
  }

  /** Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean numericLiteral(final Expression x) {
    return iz.oneOf(x, CHARACTER_LITERAL, NUMBER_LITERAL);
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
  public static boolean plainAssignment(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.ASSIGN;
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a prefix
   *         expression. */
  public static boolean prefixExpression(final ASTNode n) {
    return is(n, PREFIX_EXPRESSION);
  }

  /** Determine whether a node is a return statement
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a return
   *         statement. */
  public static boolean returnStatement(final ASTNode n) {
    return is(n, RETURN_STATEMENT);
  }

  /** Determine whether a node is a "sequencer", i.e.,
   * <code><b>return</b></code> , <code><b>break</b></code>,
   * <code><b>continue</b></code> or <code><b>throw</b></code>
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a sequencer */
  public static boolean sequencer(final ASTNode n) {
    return iz.oneOf(n, RETURN_STATEMENT, BREAK_STATEMENT, CONTINUE_STATEMENT, THROW_STATEMENT);
  }

  /** Checks if expression is simple.
   * @param x an expression
   * @return true iff argument is simple */
  public static boolean simple(final Expression x) {
    return is(x, //
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
  public static boolean simpleName(final ASTNode n) {
    return is(n, SIMPLE_NAME);
  }

  /** Determine whether a node is a singleton statement, i.e., not a block.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a singleton
   *         statement. */
  public static boolean singletonStatement(final ASTNode n) {
    return extract.statements(n).size() == 1;
  }

  /** Determine whether the "then" branch of an {@link Statement} is a single
   * statement.
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a statement */
  public static boolean singletonThen(final IfStatement s) {
    return iz.singletonStatement(step.then(s));
  }

  /** @param ¢ JD
   * @return true if the given node is a statement or false otherwise */
  public static boolean statement(final ASTNode ¢) {
    return ¢ instanceof Statement;
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a string
   *         literal */
  public static boolean stringLiteral(final ASTNode n) {
    return n != null && n.getNodeType() == STRING_LITERAL;
  }

  /** Determine whether a node is the <code><b>this</b></code> keyword
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> is the <code><b>this</b></code>
   *         keyword */
  public static boolean thisLiteral(final ASTNode n) {
    return is(n, THIS_EXPRESSION);
  }

  /** Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement */
  public static boolean thisOrNull(final Expression x) {
    return iz.oneOf(x, NULL_LITERAL, THIS_EXPRESSION);
  }

  /** Determine whether a given {@link Statement} is an {@link EmptyStatement}
   * or has nothing but empty statements in it.
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter */
  public static boolean vacuous(final Statement s) {
    return extract.statements(s).isEmpty();
  }

  /** Determine whether the 'else' part of an {@link IfStatement} is vacuous.
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the 'else' part of the parameter */
  public static boolean vacuousElse(final IfStatement s) {
    return vacuous(step.elze(s));
  }

  /** Determine whether a statement is an {@link EmptyStatement} or has nothing
   * but empty statements in it.
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter */
  public static boolean vacuousThen(final IfStatement s) {
    return vacuous(step.then(s));
  }

  /** @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a variable
   *         declaration statement. */
  public static boolean variableDeclarationStatement(final ASTNode n) {
    return is(n, VARIABLE_DECLARATION_STATEMENT);
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
    return parent != null && (step.elze(parent) == null || wizard.recursiveElze(s) == null)
        && (step.elze(parent) != null || wizard.recursiveElze(s) != null || blockRequiredInReplacement(parent, s));
  }

  static boolean isNumberLiteral(final ASTNode ¢) {
    return is(¢, NUMBER_LITERAL);
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

  static boolean literal(final ASTNode ¢, final String s) {
    return literal(az.stringLiteral(¢), s);
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
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return false;
    }
  }

  static boolean literal(final StringLiteral ¢, final String s) {
    return ¢ != null && ¢.equals(s);
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
}