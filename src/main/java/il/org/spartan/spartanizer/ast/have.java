package il.org.spartan.spartanizer.ast;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum have {
  ;
  /** Determine whether a boolean literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a boolean literal. */
  public static boolean booleanLiteral(final Expression... xs) {
    for (final Expression ¢ : xs)
      if (iz.booleanLiteral(¢))
        return true;
    return false;
  }

  /** Determine whether a boolean literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a boolean literal. */
  public static boolean booleanLiteral(final Iterable<Expression> xs) {
    for (final Expression ¢ : xs)
      if (iz.booleanLiteral(¢))
        return true;
    return false;
  }

  /** Determine whether the boolean literal <code><b>false</b></code> is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements is
   *         the boolean literal <code><b>false</b></code> */
  public static boolean falseLiteral(final List<Expression> xs) {
    for (final Expression ¢ : xs)
      if (iz.literal¢false(¢))
        return true;
    return false;
  }

  /** Determine whether a literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a literal. */
  public static boolean literal(final Expression... xs) {
    for (final Expression ¢ : xs)
      if (iz.literal(¢))
        return true;
    return false;
  }

  /** Determine whether a literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a literal. */
  public static boolean literal(final List<Expression> xs) {
    for (final Expression ¢ : xs)
      if (iz.literal(¢))
        return true;
    return false;
  }

  /** Determine whether a numerical literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a numeric literal. */
  public static boolean numericLiteral(final Expression... xs) {
    for (final Expression ¢ : xs)
      if (iz.numericLiteral(¢))
        return true;
    return false;
  }

  /** Determine whether a numerical literal is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements
   *         that is a numeric literal. */
  public static boolean numericLiteral(final Iterable<Expression> xs) {
    for (final Expression ¢ : xs)
      if (iz.numericLiteral(¢))
        return true;
    return false;
  }

  /** Determine whether the boolean literal <code><b>true</b></code> is present
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> one or more of the elements is
   *         the boolean literal <code><b>true</b></code> */
  public static boolean trueLiteral(final List<Expression> xs) {
    for (final Expression ¢ : xs)
      if (iz.literal¢true(¢))
        return true;
    return false;
  }
}
