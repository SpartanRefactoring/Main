package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import org.eclipse.jdt.core.dom.*;

/** *An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase. Specifically, this class determines precedence
 * and associativity of Java operators; data is drawn from
 * {@link "http://introcs.cs.princeton.edu/java/11precedence/"}
 * @author Yossi Gil
 * @since 2015-07-14 */
public enum Precedence {
  ;
  final static int UNDEFINED = -1;
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
    return Precedence.of(host) == Precedence.of(e2);
  }
  /** Compare precedence of two expressions.
   * @param e1 JD
   * @param e2 JD
   * @return <code><b>true</b></code> <i>iff</i> the precedence of the first
   *         parameter is strictly greater than that of the second parameter. */
  public static boolean greater(final ASTNode e1, final ASTNode e2) {
    return !Precedence.known(e1) || !Precedence.known(e2) || Precedence.of(e1) > Precedence.of(e2);
  }
  /** determine whether the precedence of a given {@link Expression} can be
   * determined.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter a legal
   *         precedence of Java. */
  public static boolean known(final ASTNode n) {
    return Is.legal(Precedence.of(n));
  }
  /** Determine the precedence of an
   * {@link org.eclipse.jdt.core.dom.Assignment.Operator}
   * @param o JD
   * @return the precedence of the parameter */
  private static int of(final Assignment.Operator o) {
    return of(o.toString());
  }
  /** Determine the precedence of an arbitrary {@link ASTNode}
   * @param n JD
   * @return the precedence of the parameter */
  public static int of(final ASTNode n) {
    return !il.org.spartan.refactoring.utils.Is.expression(n) ? UNDEFINED : Precedence.of(asExpression(n));
  }
  /** Determine the precedence of the operator present on an {@link Expression}
   * @param e JD
   * @return the precedence of the parameter */
  public static int of(final Expression e) {
    if (e == null)
      return UNDEFINED;
    switch (e.getNodeType()) {
      case INFIX_EXPRESSION:
        return of((InfixExpression) e);
      case ASSIGNMENT:
        return of((Assignment) e);
      default:
        return of(e.getClass().getSimpleName());
    }
  }
  /** Determine the precedence of an
   * {@link org.eclipse.jdt.core.dom.InfixExpression.Operator}
   * @param o JD
   * @return the precedence of the parameter */
  public static int of(final InfixExpression.Operator o) {
    return of(o.toString());
  }
  /** Determine the precedence of two expressions is the same.
   * @param e1 JD
   * @param e2 JD
   * @return the precedence of the parameter */
  public static boolean same(final Expression e1, final Expression e2) {
    return Precedence.of(e1) == Precedence.of(e2);
  }
  /** Determine whether an expression has the same precedence as that of a given
   * operator.
   * @param o JD
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the precedence of the two
   *         parameters is the same. */
  public static boolean same(final InfixExpression.Operator o, final Expression e) {
    return Precedence.of(o) == Precedence.of(e);
  }
  private static int of(final Assignment a) {
    return of(a.getOperator());
  }
  private static int of(final InfixExpression e) {
    return of(e.getOperator());
  }
  private static int of(final String key) {
    return !of.containsKey(key) ? UNDEFINED : of.get(key);
  }

  /** *An empty <code><b>enum</b></code> for fluent programming. The name should
   * say it all: The name, followed by a dot, followed by a method name, should
   * read like a sentence phrase.
   * @author Yossi Gil
   * @since 2015-07-14 */
  public enum Is {
    ;
    /** determine whether an integer falls within the legal range of precedences.
     * @param precedence JD
     * @return <code><b>true</b></code> <i>iff</i> the parameter is a legal
     *         precedence of Java. */
    public static boolean legal(final int precedence) {
      return precedence >= 1 && precedence <= 15;
    }
  }
}
