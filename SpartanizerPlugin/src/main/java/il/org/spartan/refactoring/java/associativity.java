package il.org.spartan.refactoring.java;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum associativity {
  ;
  static boolean isLeftToRigh(final Expression e) {
    return !isRightToLeft(precedence.of(e));
  }

  /** Determine whether associativity is left-to-right
   * @param o JD
   * @return <code><b>true</b></code> <i>iff</i> the associativity of the
   *         parameter is left-to-right. */
  public static boolean isLeftToRight(final InfixExpression.Operator o) {
    return isRightToLeft(precedence.of(o));
  }

  /** Determine whether associativity is right-to-left
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the associativity of parameter
   *         present on the parameter is right-to-left. */
  public static boolean isRightToLeft(final Expression e) {
    return isRightToLeft(precedence.of(e));
  }

  private static boolean isRightToLeft(final int precedence) {
    final int[] is = { 2, 3, 14, 15 };
    for (final int ¢ : is)
      if (¢ == precedence)
        return true;
    return false;
  }
}
