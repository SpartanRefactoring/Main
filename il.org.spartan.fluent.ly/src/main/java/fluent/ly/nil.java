package fluent.ly;

import java.util.function.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-10 */
public interface nil {
  interface On<T, R> {
    R on(T t);
  }

  @SuppressWarnings("unused") static <T> T forgetting(final Object _1, final Object... _2) {
    return null;
  }
  static <T, R> On<T, R> guardingly(final Function<T, R> f) {
    return λ -> λ == null ? null : f.apply(λ);
  }
  @SuppressWarnings("unused") static <T> T ignoring(final boolean __) {
    return null;
  }
  @SuppressWarnings("unused") static <T> T ignoring(final double __) {
    return null;
  }
  @SuppressWarnings("unused") static <T> T ignoring(final long __) {
    return null;
  }

  interface Operand<T> extends Supplier<T> {
    default <R> Operand<R> to(final Function<T, R> f) {
      final T t = Operand.this.get();
      final R $ = t == null ? null : f.apply(t);
      return () -> $;
    }
  }

  static <T> Operand<T> guardingly(final T ¢) {
    return () -> ¢;
  }

  interface U<END, T1> {
    default <T2> U<END, T2> on(final Function<T2, T1> ¢) {
      return () -> ¢.andThen(U.this.lastOn());
    }
    default END on(final T1 ¢) {
      return lastOn().apply(¢);
    }
    Function<T1, END> lastOn();
  }

  static <T, R> U<R, T> cautiously(final Function<T, R> ¢) {
    return () -> ¢;
  }
}
