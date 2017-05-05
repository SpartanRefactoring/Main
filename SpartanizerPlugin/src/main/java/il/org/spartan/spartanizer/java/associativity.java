package il.org.spartan.spartanizer.java;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum associativity {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Determine whether associativity is left-to-right
   * @param o JD
   * @return whether the associativity of the parameter is left-to-right. */
  public static boolean isLeftToRight(final InfixExpression.Operator ¢) {
    return isRightToLeft(precedence.of(¢));
  }
  /** Determine whether associativity is right-to-left
   * @param x JD
   * @return whether the associativity of parameter present on the parameter is
   *         right-to-left. */
  public static boolean isRightToLeft(final Expression ¢) {
    return isRightToLeft(precedence.of(¢));
  }
  static boolean isLeftToRight(final Expression ¢) {
    return !isRightToLeft(precedence.of(¢));
  }
  private static boolean isRightToLeft(final int precedence) {
    return intIsIn(precedence, 2, 3, 14, 15);
  }
}
