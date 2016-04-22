package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.asBlock;
import static il.org.spartan.refactoring.utils.Funcs.asIfStatement;
import static il.org.spartan.refactoring.utils.Funcs.asInfixExpression;
import static il.org.spartan.refactoring.utils.Funcs.asPrefixExpression;
import static il.org.spartan.refactoring.utils.Funcs.elze;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.parent;
import static il.org.spartan.refactoring.utils.Funcs.then;
import static il.org.spartan.utils.Utils.in;
import static il.org.spartan.utils.Utils.intIsIn;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.BOOLEAN_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.BREAK_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CAST_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CHARACTER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CLASS_INSTANCE_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONDITIONAL_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CONTINUE_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.INSTANCEOF_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.POSTFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PREFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.QUALIFIED_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.RETURN_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.SIMPLE_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.STRING_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.THIS_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.THROW_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER_EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS_EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.NOT_EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.PLUS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.REMAINDER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.XOR;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import il.org.spartan.misc.Wrapper;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum Is {
  ;
  /**
   * Determine whether a variable declaration is final or not
   *
   * @param s some declaration
   * @return <code><b>true</b></code> <i>iff</i> the variable is declared as
   *         final
   */
  public static boolean _final(final VariableDeclarationStatement s) {
    return (Modifier.FINAL & s.getModifiers()) != 0;
  }
  /**
   * @param n the statement or block to check if it is an assignment
   * @return <code><b>true</b></code> if the parameter an assignment or false if
   *         the parameter not or if the block Contains more than one statement
   */
  public static boolean assignment(final ASTNode n) {
    return is(n, ASSIGNMENT);
  }
  /**
   * Determine whether a node is a {@link Block}
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement
   */
  public static boolean block(final ASTNode n) {
    return is(n, BLOCK);
  }
  static boolean blockEssential(final IfStatement s) {
    if (s == null)
      return false;
    final Block b = asBlock(parent(s));
    if (b == null)
      return false;
    final IfStatement parent = asIfStatement(parent(b));
    return parent != null && then(parent) == b && (elze(parent) == null || elze(s) == null)
        && (elze(parent) != null || elze(s) != null || blockRequiredInReplacement(parent, s));
  }
  /**
   * Determine whether a block (curly parenthesis) is essential, even if it
   * contains a single statement as in
   *
   * <pre>
   * <b>if</b> (a) { // <i> cannot remove</i>
   *    <b>if</b> (b) // <i> inner <b>if</b> </i>
   *      f();
   * } // <i> cannot remove</i>
   *  <b>else</b> // <i> if block removed, will attach to inner <b>if</b> </i>
   *      g();
   * </pre>
   *
   * @param s JD
   * @return n <code><b>true</b></code> <i>iff</i> the parameter is an <b>if</b>
   *         statement which must be contained in a block to prevent an
   *         <code><b>else</b></code> clause from attaching to it.
   */
  public static boolean blockEssential(final Statement s) {
    return blockEssential(asIfStatement(s));
  }
  public static boolean blockRequired(final IfStatement s) {
    return blockRequiredInReplacement(s, s);
  }
  public static boolean blockRequired(final Statement s) {
    final IfStatement s1 = asIfStatement(s);
    return blockRequiredInReplacement(s1, s1);
  }
  public static boolean blockRequiredInReplacement(final IfStatement old, final IfStatement newIf) {
    if (newIf == null || old != newIf && elze(old) == null == (elze(newIf) == null))
      return false;
    final IfStatement parent = asIfStatement(parent(old));
    return parent != null && then(parent) == old && (elze(parent) == null || elze(newIf) == null)
        && (elze(parent) != null || elze(newIf) != null || blockRequiredInReplacement(parent, newIf));
  }
  /**
   * Determine whether a node is a boolean literal
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a boolean
   *         literal
   */
  public static boolean booleanLiteral(final ASTNode n) {
    return is(n, BOOLEAN_LITERAL);
  }
  /**
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a comparison
   *         expression.
   */
  public static boolean comparison(final InfixExpression e) {
    return in(e.getOperator(), EQUALS, GREATER, GREATER_EQUALS, LESS, LESS_EQUALS, NOT_EQUALS);
  }
  /**
   * @param es JD
   * @return <code><b>true</b></code> <i>iff</i> one of the parameters is a
   *         conditional or parenthesized conditional expression
   */
  public static boolean conditional(final Expression... es) {
    for (final Expression e : es) {
      if (e == null)
        continue;
      switch (e.getNodeType()) {
        default:
          break;
        case CONDITIONAL_EXPRESSION:
          return true;
        case PARENTHESIZED_EXPRESSION:
          if (((ParenthesizedExpression) e).getExpression().getNodeType() == CONDITIONAL_EXPRESSION)
            return true;
      }
    }
    return false;
  }
  /**
   * Check whether an expression is a "conditional and" (&&)
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_AND}
   */
  public static boolean conditionalAnd(final InfixExpression e) {
    return e.getOperator() == CONDITIONAL_AND;
  }
  /**
   * Check whether an expression is a "conditional or" (||)
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR}
   */
  public static boolean conditionalOr(final Expression e) {
    return conditionalOr(asInfixExpression(e));
  }
  /**
   * Check whether an expression is a "conditional or" (||)
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose operator is
   *         {@link org.eclipse.jdt.core.dom.InfixExpression.Operator#CONDITIONAL_OR}
   */
  public static boolean conditionalOr(final InfixExpression e) {
    return e != null && e.getOperator() == CONDITIONAL_OR;
  }
  /**
   * Determine whether a node is a "specific", i.e., <code><b>null</b></code> or
   * <code><b>this</b></code> or literal.
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a "specific"
   */
  public static boolean constant(final Expression e) {
    switch (e.getNodeType()) {
      case CHARACTER_LITERAL:
      case NUMBER_LITERAL:
      case NULL_LITERAL:
      case THIS_EXPRESSION:
        return true;
      case PREFIX_EXPRESSION:
        return Is.constant(Extract.core(((PrefixExpression) e).getOperand()));
      default:
        return false;
    }
  }
  /**
   * Check whether the operator of an expression is susceptible for applying one
   * of the two de Morgan laws.
   *
   * @param e InfixExpression
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply.
   */
  public static boolean deMorgan(final InfixExpression e) {
    return Is.deMorgan(e.getOperator());
  }
  /**
   * Check whether an operator is susceptible for applying one of the two de
   * Morgan laws.
   *
   * @param o JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an operator on
   *         which the de Morgan laws apply.
   */
  public static boolean deMorgan(final Operator o) {
    return in(o, CONDITIONAL_AND, CONDITIONAL_OR);
  }
  public static boolean deterministic(final Expression e) {
    if (!sideEffectFree(e))
      return false;
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.TRUE);
    e.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ArrayCreation _) {
        $.set(Boolean.FALSE);
        return false;
      }
    });
    return $.get().booleanValue();
  }
  /**
   * Determine whether a node is an {@link EmptyStatement}
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link EmptyStatement}
   */
  public static boolean emptyStatement(final ASTNode n) {
    return is(n, EMPTY_STATEMENT);
  }
  /**
   * Determine whether a node is an "expression statement"
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link ExpressionStatement} statement
   */
  public static boolean expression(final ASTNode n) {
    return n != null && n instanceof Expression;
  }
  /**
   * Determine whether a node is an "expression statement"
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an
   *         {@link ExpressionStatement} statement
   */
  public static boolean expressionStatement(final ASTNode n) {
    return is(n, EXPRESSION_STATEMENT);
  }
  /**
   * @param o The operator to check
   * @return True - if the operator have opposite one in terms of operands swap.
   */
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
  /**
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an infix
   *         expression.
   */
  public static boolean infix(final ASTNode n) {
    return is(n, INFIX_EXPRESSION);
  }
  private static boolean is(final ASTNode n, final int type) {
    return n != null && type == n.getNodeType();
  }
  /**
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the node is an Expression
   *         Statement of type Post or Pre Expression with ++ or -- operator
   *         false if node is not an Expression Statement or its a Post or Pre
   *         fix expression that its operator is not ++ or --
   */
  public static boolean isNodeIncOrDecExp(final ASTNode n) {
    switch (n.getNodeType()) {
      case EXPRESSION_STATEMENT:
        return isNodeIncOrDecExp(((ExpressionStatement) n).getExpression());
      case POSTFIX_EXPRESSION:
        return in(((PostfixExpression) n).getOperator(), PostfixExpression.Operator.INCREMENT,
            PostfixExpression.Operator.DECREMENT);
      case PREFIX_EXPRESSION:
        return in(asPrefixExpression(n).getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
      default:
        return false;
    }
  }
  private static boolean isOneOf(final int i, final int... is) {
    for (final int j : is)
      if (i == j)
        return true;
    return false;
  }
  /**
   * Determine whether an item is the last one in a list
   *
   * @param t a list item
   * @param ts a list
   * @return <code><b>true</b></code> <i>iff</i> the item is found in the list
   *         and it is the last one in it.
   */
  public static <T> boolean last(final T t, final List<T> ts) {
    return ts.indexOf(t) == ts.size() - 1;
  }
  /**
   * @param n Expression node
   * @return <code><b>true</b></code> <i>iff</i> the Expression is literal
   */
  public static boolean literal(final ASTNode n) {
    return intIsIn(n.getNodeType(), //
        NULL_LITERAL, //
        CHARACTER_LITERAL, //
        NUMBER_LITERAL, //
        STRING_LITERAL, //
        BOOLEAN_LITERAL //
    );
  }
  /**
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter return a literal
   */
  public static boolean literal(final ReturnStatement s) {
    return literal(s.getExpression());
  }
  /**
   * Determine whether a node is a {@link MethodDeclaration}
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation.
   */
  public static boolean methodDeclaration(final ASTNode n) {
    return is(n, METHOD_DECLARATION);
  }
  /**
   * Determine whether a node is a {@link MethodInvocation}
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a method
   *         invocation.
   */
  public static boolean methodInvocation(final ASTNode n) {
    return is(n, METHOD_INVOCATION);
  }
  public static boolean negative(final Expression e) {
    return e.toString().startsWith("-") || e instanceof PrefixExpression && negative((PrefixExpression) e);
  }
  public static boolean negative(final PrefixExpression e) {
    return e.getOperator() == PrefixExpression.Operator.MINUS;
  }
  /**
   * Determine whether a node is an infix expression whose operator is
   * non-associative.
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a node which
   *         is an infix expression whose operator is
   */
  public static boolean nonAssociative(final ASTNode n) {
    return nonAssociative(asInfixExpression(n));
  }
  private static boolean nonAssociative(final InfixExpression e) {
    return e != null && in(e.getOperator(), MINUS, DIVIDE, REMAINDER);
  }
  /**
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose type is provably not of type {@link String}, in the sense
   *         used in applying the <code>+</code> operator to concatenate
   *         strings. concatenation.
   */
  public static boolean notString(final Expression e) {
    return notStringSelf(e) || notStringUp(e) || notStringDown(asInfixExpression(e));
  }
  static boolean notStringDown(final Expression e) {
    return notStringSelf(e) || notStringDown(asInfixExpression(e));
  }
  static boolean notStringDown(final InfixExpression e) {
    return e != null && (e.getOperator() != PLUS || Are.notString(Extract.allOperands(e)));
  }
  static boolean notStringSelf(final Expression e) {
    return intIsIn(e.getNodeType(), //
        ARRAY_CREATION, //
        BOOLEAN_LITERAL, //
        CHARACTER_LITERAL, //
        INSTANCEOF_EXPRESSION, //
        NULL_LITERAL, // null + null is an error, not a string.
        NUMBER_LITERAL, //
        PREFIX_EXPRESSION //
    //
    );
  }
  private static boolean notStringUp(final Expression e) {
    for (ASTNode context = e.getParent(); context != null; context = context.getParent())
      switch (context.getNodeType()) {
        case INFIX_EXPRESSION:
          if (asInfixExpression(context).getOperator().equals(PLUS))
            continue;
          return true;
        case ARRAY_ACCESS:
        case PREFIX_EXPRESSION:
        case POSTFIX_EXPRESSION:
          return true;
        case PARENTHESIZED_EXPRESSION:
          continue;
        default:
          return false;
      }
    return false;
  }
  /**
   * Determine whether a node is the <code><b>null</b></code> keyword
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i>is thee <code><b>null</b></code>
   *         literal
   */
  public static boolean null_(final ASTNode n) {
    return is(n, NULL_LITERAL);
  }
  /**
   * Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement
   */
  public static boolean numericLiteral(final Expression e) {
    return Is.oneOf(e, CHARACTER_LITERAL, NUMBER_LITERAL);
  }
  /**
   * Determine whether the type of an {@link ASTNode} node is one of given list
   *
   * @param n a node
   * @param types a list of types
   * @return <code><b>true</b></code> <i>iff</i> function #ASTNode.getNodeType
   *         returns one of the types provided as parameters
   */
  public static boolean oneOf(final ASTNode n, final int... types) {
    return n != null && isOneOf(n.getNodeType(), types);
  }
  /**
   * @param a the assignment who's operator we want to check
   * @return true is the assignment's operator is assign
   */
  public static boolean plainAssignment(final Assignment a) {
    return a != null && a.getOperator() == Assignment.Operator.ASSIGN;
  }
  /**
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a prefix
   *         expression.
   */
  public static boolean prefix(final ASTNode n) {
    return is(n, PREFIX_EXPRESSION);
  }
  /**
   * Determine whether a node is a return statement
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a return
   *         statement.
   */
  public static boolean return_(final ASTNode n) {
    return is(n, RETURN_STATEMENT);
  }
  /**
   * Determine whether a node is a "sequencer", i.e., <code><b>return</b></code>
   * , <code><b>break</b></code>, <code><b>continue</b></code> or
   * <code><b>throw</b></code>
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a sequencer
   */
  public static boolean sequencer(final ASTNode n) {
    return Is.oneOf(n, RETURN_STATEMENT, BREAK_STATEMENT, CONTINUE_STATEMENT, THROW_STATEMENT);
  }
  /**
   * Determine whether the evaluation of an expression is guaranteed to be free
   * of any side effects.
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose computation is guaranteed to be free of any side effects.
   */
  public static boolean sideEffectFree(final Expression e) {
    if (e == null)
      return true;
    switch (e.getNodeType()) {
      case STRING_LITERAL:
      case NULL_LITERAL:
      case NUMBER_LITERAL:
      case THIS_EXPRESSION:
      case SIMPLE_NAME:
      case TYPE_LITERAL:
      case CHARACTER_LITERAL:
      case BOOLEAN_LITERAL:
      case QUALIFIED_NAME:
      case FIELD_ACCESS:
      case SUPER_FIELD_ACCESS:
        return true;
      case SUPER_CONSTRUCTOR_INVOCATION:
      case SUPER_METHOD_INVOCATION:
      case METHOD_INVOCATION:
      case CLASS_INSTANCE_CREATION:
      case ASSIGNMENT:
      case POSTFIX_EXPRESSION:
        return false;
      case ARRAY_CREATION:
        return sideEffectFreeArrayCreation((ArrayCreation) e);
      case ARRAY_ACCESS:
        final ArrayAccess x = (ArrayAccess) e;
        return sideEffectsFree(x.getArray(), x.getIndex());
      case CAST_EXPRESSION:
        final CastExpression c = (CastExpression) e;
        return sideEffectFree(c.getExpression());
      case INSTANCEOF_EXPRESSION:
        return sideEffectFree(left((InstanceofExpression) e));
      case PREFIX_EXPRESSION:
        return sideEffectFreePrefixExpression((PrefixExpression) e);
      case PARENTHESIZED_EXPRESSION:
        return sideEffectFree(((ParenthesizedExpression) e).getExpression());
      case INFIX_EXPRESSION:
        return sideEffectsFree(Extract.allOperands((InfixExpression) e));
      case CONDITIONAL_EXPRESSION:
        final ConditionalExpression ce = (ConditionalExpression) e;
        return sideEffectsFree(ce.getExpression(), ce.getThenExpression(), ce.getElseExpression());
      case ARRAY_INITIALIZER:
        return sideEffectsFree(((ArrayInitializer) e).expressions());
      default:
        System.err.println("Missing handler for class: " + e.getClass().getSimpleName());
        return false;
    }
  }
  static boolean sideEffectFreeArrayCreation(final ArrayCreation c) {
    final ArrayInitializer i = c.getInitializer();
    return sideEffectsFree(c.dimensions()) && (i == null || sideEffectsFree(i.expressions()));
  }
  static boolean sideEffectFreePrefixExpression(final PrefixExpression e) {
    return in(e.getOperator(), PrefixExpression.Operator.PLUS, PrefixExpression.Operator.MINUS,
        PrefixExpression.Operator.COMPLEMENT, PrefixExpression.Operator.NOT) && sideEffectFree(e.getOperand());
  }
  private static boolean sideEffectsFree(final Expression... es) {
    for (final Expression e : es)
      if (!sideEffectFree(e))
        return false;
    return true;
  }
  private static boolean sideEffectsFree(final List<?> os) {
    for (final Object o : os)
      if (o == null || !sideEffectFree(Funcs.asExpression((ASTNode) o)))
        return false;
    return true;
  }
  /**
   * Determine whether an {@link Expression} is so basic that it never needs to
   * be placed in parenthesis.
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is so basic that
   *         it never needs to be placed in parenthesis.
   */
  public static boolean simple(final Expression e) {
    return in(e.getClass(), //
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
  /**
   * Determine whether a node is a simple name
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a simple name
   */
  public static boolean simpleName(final ASTNode n) {
    return is(n, SIMPLE_NAME);
  }
  /**
   * Determine whether a node is a singleton statement, i.e., not a block.
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a singleton
   *         statement.
   */
  public static boolean singletonStatement(final ASTNode n) {
    return Extract.statements(n).size() == 1;
  }
  /**
   * Determine whether the "then" branch of an {@link Statement} is a single
   * statement.
   *
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a statement
   */
  public static boolean singletonThen(final IfStatement s) {
    return Is.singletonStatement(then(s));
  }
  /**
   * Determine whether a node is a {@link Statement}
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a statement
   */
  public static boolean statement(final ASTNode n) {
    return n instanceof Statement;
  }
  /**
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a string
   *         literal
   */
  public static boolean stringLiteral(final ASTNode n) {
    return n != null && n.getNodeType() == STRING_LITERAL;
  }
  /**
   * Determine whether a node is the <code><b>this</b></code> keyword
   *
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> is the <code><b>this</b></code>
   *         keyword
   */
  public static boolean this_(final ASTNode n) {
    return is(n, THIS_EXPRESSION);
  }
  /**
   * Determine whether a node is <code><b>this</b></code> or
   * <code><b>null</b></code>
   *
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a block
   *         statement
   */
  public static boolean thisOrNull(final Expression e) {
    return Is.oneOf(e, NULL_LITERAL, THIS_EXPRESSION);
  }
  /**
   * Determine whether a given {@link Statement} is an {@link EmptyStatement} or
   * has nothing but empty statements in it.
   *
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter
   */
  public static boolean vacuous(final Statement s) {
    return Extract.statements(s).isEmpty();
  }
  /**
   * Determine whether the 'else' part of an {@link IfStatement} is vacuous.
   *
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the 'else' part of the parameter
   */
  public static boolean vacuousElse(final IfStatement s) {
    return vacuous(elze(s));
  }
  /**
   * Determine whether a statement is an {@link EmptyStatement} or has nothing
   * but empty statements in it.
   *
   * @param s JD
   * @return <code><b>true</b></code> <i>iff</i> there are no non-empty
   *         statements in the parameter
   */
  public static boolean vacuousThen(final IfStatement s) {
    return vacuous(then(s));
  }
  /**
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a variable
   *         declaration statement.
   */
  public static boolean variableDeclarationStatement(final ASTNode n) {
    return is(n, VARIABLE_DECLARATION_STATEMENT);
  }
}
