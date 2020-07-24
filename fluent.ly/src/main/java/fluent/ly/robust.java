package fluent.ly;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-08 */
public interface robust {
  static void ly(final Runnable r, final Consumer<Exception> c) {
    robust.lyNull(() -> nulling.ly(r::run), c);
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
  static boolean lyFalse(final BooleanSupplier s, final Consumer<Exception> c) {
    try {
      return s.getAsBoolean();
    } catch (final Exception $) {
      c.accept($);
      return false;
    }
  }
  static <T> T lyNull(final Supplier<T> t) {
    return robust.ly(t, __ -> null);
  }
  static <T> T lyNull(final Supplier<T> t, final Consumer<Exception> c) {
    return robust.ly(t, λ -> nulling.ly(() -> c.accept(λ)));
  }
  static <T> T lyNull(final Supplier<T> t, final Runnable r) {
    return robust.ly(t, __ -> nulling.ly(r));
  }
  static boolean lyTrue(final BooleanSupplier $, final Consumer<Exception> c) {
    try {
      return $.getAsBoolean();
    } catch (final Exception ¢) {
      c.accept(¢);
      return true;
    }
  }
  static boolean lyTrue(final Runnable r, final Consumer<Exception> c) {
    try {
      r.run();
    } catch (final Exception ¢) {
      c.accept(¢);
    }
    return true;
  }
}
