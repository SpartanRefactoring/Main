package il.org.spartan.refactoring.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

/** *An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase. Specifically, this class determines precedence
 * and associativity of Java operators; data is drawn from
 * {@link "http://introcs.cs.princeton.edu/java/11precedence/"}
 * @author Yossi Gil
 * @since 2015-07-14 */
public enum precedence {
  ;
  public final static int UNDEFINED = -1;

  private static final ChainStringToIntegerMap of = new ChainStringToIntegerMap()//
      .putOn(1, "[]", ".", "() invoke", "++ post", "-- post", "MethodInvocation", "PostfixExpression", "ArrayAccess", "FieldAccess", "QualifiedName") //
      .putOn(2, "++ pre", "-- pre", "+ unary", "- unary", "!", "~", "PrefixExpression") //
      .putOn(3, "() cast", "new", "ArrayCreation", "ClassInstanceCreation", "CastExpression") //
      .putOn(4, "*", "/", "%") // multiplicative
      .putOn(5, "+", "-") // additive
      .putOn(6, ">>", "<<", ">>>") // shift
      .putOn(7, "<", "<=", ">", ">=", "instanceof", "InstanceofExpression") // relational
      .putOn(8, "==", "!=") // equality
      .putOn(9, "&") // bitwise AND
      .putOn(10, "^") // bitwise XOR
      .putOn(11, "|") // bitwise OR
      .putOn(12, "&&") // conditional AND
      .putOn(13, "||") // conditional OR
      .putOn(14, "?", ":", "ConditionalExpression") // conditional
      .putOn(15, "=", // assignment
          "+=", "-=", // assignment, additive
          "*=", "/=", "%=", // assignment, multiplicative
          "&=", "^=", "|=", // assignment, bitwise
          "<<=", ">>=", ">>>="// assignment, shift
  );
  /** Compare precedence of two expressions.
   * @param host JD
   * @param e2 JD
   * @return <code><b>true</b></code> <i>iff</i> the precedence of the first
   *         parameter is equal to that of the second parameter. */
  public static boolean equal(final ASTNode host, final ASTNode e2) {
    return precedence.of(host) == precedence.of(e2);
  }

  /** Compare precedence of two expressions.
   * @param e1 JD
   * @param e2 JD
   * @return <code><b>true</b></code> <i>iff</i> the precedence of the first
   *         parameter is strictly greater than that of the second parameter. */
  public static boolean greater(final ASTNode e1, final ASTNode e2) {
    return !precedence.known(e1) || !precedence.known(e2) || precedence.of(e1) > precedence.of(e2);
  }

  /** determine whether the precedence of a given {@link Expression} can be
   * determined.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter a legal
   *         precedence of Java. */
  public static boolean known(final ASTNode n) {
    return is.legal(precedence.of(n));
  }

  /** Determine the precedence of an arbitrary {@link ASTNode}
   * @param n JD
   * @return precedence of the parameter */
  public static int of(final ASTNode n) {
    return !iz.expression(n) ? UNDEFINED : precedence.of(az.expression(n));
  }

  /** Determine the precedence of the operator present on an {@link Expression}
   * @param x JD
   * @return precedence of the parameter */
  public static int of(final Expression x) {
    if (x == null)
      return UNDEFINED;
    switch (x.getNodeType()) {
      case INFIX_EXPRESSION:
        return of((InfixExpression) x);
      case ASSIGNMENT:
        return of((Assignment) x);
      default:
        return of(x.getClass().getSimpleName());
    }
  }

  /** Determine the precedence of an
   * {@link org.eclipse.jdt.core.dom.InfixExpression.Operator}
   * @param o JD
   * @return precedence of the parameter */
  public static int of(final InfixExpression.Operator o) {
    return of("" + o);
  }

  /** Determine the precedence of two expressions is the same.
   * @param e1 JD
   * @param e2 JD
   * @return precedence of the parameter */
  public static boolean same(final Expression e1, final Expression e2) {
    return precedence.of(e1) == precedence.of(e2);
  }

  /** Determine whether an expression has the same precedence as that of a given
   * operator.
   * @param o JD
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the precedence of the two
   *         parameters is the same. */
  public static boolean same(final InfixExpression.Operator o, final Expression x) {
    return precedence.of(o) == precedence.of(x);
  }

  private static int of(final Assignment a) {
    return of(a.getOperator());
  }

  /** Determine the precedence of an
   * {@link org.eclipse.jdt.core.dom.Assignment.Operator}
   * @param o JD
   * @return precedence of the parameter */
  private static int of(final Assignment.Operator o) {
    return of("" + o);
  }

  private static int of(final InfixExpression x) {
    return of(x.getOperator());
  }

  private static int of(final String key) {
    return !of.containsKey(key) ? UNDEFINED : of.get(key);
  }

  /** *An empty <code><b>enum</b></code> for fluent programming. The name should
   * say it all: The name, followed by a dot, followed by a method name, should
   * read like a sentence phrase.
   * @author Yossi Gil
   * @since 2015-07-14 */
  public enum is {
    ;
    /** determine whether an integer falls within the legal range of
     * precedences.
     * @param precedence JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is a legal
     *         precedence of Java. */
    public static boolean legal(final int precedence) {
      return precedence >= 1 && precedence <= 15;
    }
  }
}
