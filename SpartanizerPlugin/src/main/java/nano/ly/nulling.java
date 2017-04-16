package nano.ly;

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
    return Null.ignoring(¢.getAsDouble());
  }

  static <T> T ly(final LongSupplier ¢) {
    return Null.ignoring(¢.getAsLong());
  }

  static <T> T ly(final IntSupplier ¢) {
    return Null.ignoring(¢.getAsInt());
  }

  static <T> T ly(final BooleanSupplier ¢) {
    return Null.ignoring(¢.getAsBoolean());
  }

  static <T, R> T ly(final Supplier<R> ¢) {
    return Null.forgetting(¢.get());
  }
}
