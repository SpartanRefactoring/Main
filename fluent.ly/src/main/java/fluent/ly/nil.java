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
    default <R> Operand<R> to(Function<T, R> f) {
      T t = Operand.this.get();
      R $ = t == null ? null : f.apply(t);
      return new Operand<R>() {
        @Override public R get() {
          return $;
        }
      };
    }
  }

  static <T> Operand<T> guardingly(T ¢) {
    return new Operand<T>() {
      @Override public T get() {
        return ¢;
      }
    };
  }

  interface U<END, T1> {
    default <T2> U<END, T2> on(Function<T2, T1> ¢) {
      return new U<END, T2>() {
        @Override public Function<T2, END> lastOn() {
          return ¢.andThen(U.this.lastOn());
        }
      };
    }
    default END on(T1 ¢) {
      return lastOn().apply(¢);
    }
    Function<T1, END> lastOn();
  }

  static <T, R> U<R, T> cautiously(Function<T, R> ¢) {
    return new U<R, T>() {
      @Override public Function<T, R> lastOn() {
        return ¢;
      }
    };
  }
}
