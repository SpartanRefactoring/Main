package il.org.spartan.utils;

import java.util.function.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface run {
  static <T> T nullifying(final Runnable ¢) {
    ¢.run();
    return the.null¢();
  }
  static <T> T nullifying(final DoubleSupplier ¢) {
    return ignoring.null¢(¢.getAsDouble()); 
  }
  static <T> T nullifying(final LongSupplier ¢) {
    return ignoring.null¢(¢.getAsLong()); 
  }
  static <T> T nullifying(final IntSupplier ¢) {
    return ignoring.null¢(¢.getAsInt()); 
  }

  static <T> T nullifying(final BooleanSupplier ¢) {
    return ignoring.null¢(¢.getAsBoolean()); 
  }
 static <T,R> T nullifying(final Supplier<R> ¢) {
    return ignoring.null¢(¢.get()); 
  }
}
