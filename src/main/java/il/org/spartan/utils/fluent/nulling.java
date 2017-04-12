package il.org.spartan.utils.fluent;

import java.util.function.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface nulling {
  static <T> T ly(final Runnable ¢) {
    ¢.run();
    return the.null¢();
  }

  static <T> T ly(final DoubleSupplier ¢) {
    return null¢.ignoring(¢.getAsDouble());
  }

  static <T> T ly(final LongSupplier ¢) {
    return null¢.ignoring(¢.getAsLong());
  }

  static <T> T ly(final IntSupplier ¢) {
    return null¢.ignoring(¢.getAsInt());
  }

  static <T> T ly(final BooleanSupplier ¢) {
    return null¢.ignoring(¢.getAsBoolean());
  }

  static <T, R> T ly(final Supplier<R> ¢) {
    return null¢.ignoring(¢.get());
  }
}
