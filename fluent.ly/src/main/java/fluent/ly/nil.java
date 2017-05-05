package fluent.ly;

import java.util.function.*;

import org.eclipse.jdt.annotation.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-10 */
public interface nil {
  interface On<T, R> {
    R on(T t);
  }

  @SuppressWarnings("unused") static < T> T forgetting(final Object _1, final Object... _2) {
    return null;
  }
  static <T, R> On<T, R> guardingly(final Function<T, R> f) {
    return λ -> λ == null ? null : f.apply(λ);
  }
  @SuppressWarnings("unused") static < T> T ignoring(final boolean __) {
    return null;
  }
  @SuppressWarnings("unused") static < T> T ignoring(final double __) {
    return null;
  }
  @SuppressWarnings("unused") static < T> T ignoring(final long __) {
    return null;
  }
}
