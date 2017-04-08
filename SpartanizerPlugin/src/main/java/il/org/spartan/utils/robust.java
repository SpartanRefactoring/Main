package il.org.spartan.utils;

import java.util.function.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-08 */
public interface robust {
  static boolean lyFalse(final BooleanSupplier s, final Consumer<Exception> x) {
    try {
      return s.getAsBoolean();
    } catch (final Exception $) {
      x.accept($);
      return false;
    }
  }

  static boolean lyTrue(final Runnable r, final Consumer<Exception> x) {
    try {
      r.run();
    } catch (final Exception $) {
      x.accept($);
    }
    return true;
  }

  static boolean lyTrue(final BooleanSupplier s, final Consumer<Exception> x) {
    try {
      return s.getAsBoolean();
    } catch (final Exception $) {
      x.accept($);
      return true;
    }
  }

  static void ly(final Runnable r, final Consumer<Exception> x) {
    robust.lyNull(() -> {
      r.run();
      return null;
    }, x);
  }

  static void ly(final Runnable r, final Runnable x) {
    robust.ly(r, __ -> x.run());
  }

  static <T> T ly(final Supplier<T> t, final Function<Exception, T> f) {
    try {
      return t.get();
    } catch (final Exception $) {
      return f.apply($);
    }
  }

  static <T> T lyNull(final Supplier<T> t) {
    return robust.ly(t, __ -> null);
  }

  static <T> T lyNull(final Supplier<T> t, final Runnable r) {
    return robust.ly(t, __ -> {
      r.run();
      return null;
    });
  }

  static <T> T lyNull(final Supplier<T> t, final Consumer<Exception> x) {
    return robust.ly(t, λ -> {
      x.accept(λ);
      return null;
    });
  }
}
