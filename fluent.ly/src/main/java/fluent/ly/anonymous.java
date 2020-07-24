package fluent.ly;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * TODO Yossi Gil: document class
 *
 * @author Yossi Gil
 * @since 2017-04-12
 */
public interface anonymous {
  static boolean ly(final BooleanSupplier $) {
    return $.getAsBoolean();
  }

  static double ly(final DoubleSupplier $) {
    return $.getAsDouble();
  }

  static int ly(final IntSupplier $) {
    return $.getAsInt();
  }

  static long ly(final LongSupplier $) {
    return $.getAsLong();
  }

  static <T> T ly(final Supplier<T> $) {
    return $.get();
  }
}
