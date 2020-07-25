package il.org.spartan.spartanizer.ast.navigate;

import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.Expression;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum have {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Determine whether a boolean literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a boolean literal. */
  public static boolean booleanLiteral(final Expression... ¢) {
    return booleanLiteral(Stream.of(¢));
  }
  /** Determine whether a boolean literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a boolean literal. */
  public static boolean booleanLiteral(final Iterable<Expression> ¢) {
    return booleanLiteral(az.stream(¢));
  }
  public static boolean booleanLiteral(final Stream<Expression> ¢) {
    return ¢.anyMatch(iz::booleanLiteral);
  }
  /** Determine whether the boolean literal {@code false} is present
   * @param ¢ JD
   * @return whether one or more of the elements is the boolean literal
   *         {@code false} */
  public static boolean falseLiteral(final Iterable<Expression> ¢) {
    return az.stream(¢).anyMatch(iz.literal::false¢);
  }
  /** Determine whether a literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a literal. */
  public static boolean literal(final Expression... ¢) {
    return Stream.of(¢).anyMatch(iz::literal);
  }
  /** Determine whether a literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a literal. */
  public static boolean literal(final Collection<Expression> ¢) {
    return ¢.stream().anyMatch(iz::literal);
  }
  /** Determine whether a numerical literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a numeric literal. */
  public static boolean numericLiteral(final Expression... ¢) {
    return Stream.of(¢).anyMatch(iz::numericLiteral);
  }
  /** Determine whether a numerical literal is present
   * @param ¢ JD
   * @return whether one or more of the elements that is a numeric literal. */
  public static boolean numericLiteral(final Iterable<Expression> ¢) {
    return az.stream(¢).anyMatch(iz::numericLiteral);
  }
  /** Determine whether the boolean literal {@code true} is present
   * @param ¢ JD
   * @return whether one or more of the elements is the boolean literal
   *         {@code true} */
  public static boolean trueLiteral(final Collection<Expression> ¢) {
    return ¢.stream().anyMatch(iz.literal::true¢);
  }
}
