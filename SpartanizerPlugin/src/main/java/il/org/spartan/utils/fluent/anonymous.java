package il.org.spartan.utils.fluent;

import java.util.function.*;

/** TODO Yossi Gil: document class 
 * 
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface anonymous {

  static <T> T ly(final Supplier<T> $) {
    return $.get();
  }
  static double ly(final DoubleSupplier $) {
    return $.getAsDouble();
  }
  static long ly(final LongSupplier $) {
    return $.getAsLong();
  }
  static int ly(final IntSupplier $) {
    return $.getAsInt();
  }

  static boolean ly(final BooleanSupplier $) {
    return $.getAsBoolean();
  }

}
