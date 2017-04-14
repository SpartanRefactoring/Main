package il.org.spartan.utils.fluent;

import il.org.spartan.iteration.closures.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-10 */
public interface null¢ {
  static <T, R> On<T, R> guardingly(final Function<T, R> f) {
    return λ -> λ == null ? null : f.eval(λ);
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

  @SuppressWarnings("unused") static <T> T ignoring(final Object ____, final Object... __) {
    return null;
  }

  interface On<T, R> {
    R on(T t);
  }
}
