/**
 *
 */
package il.org.spatan.iteration;

import static il.org.spartan.utils.____.*;

/** @param <T> Type over which we shall iterate * @author Yossi Gil
 * @since 01/05/2011 */
public abstract class Iteration<T> {
  public void at(final T t) {
    unused(t);
  }

  public void epilog(final T t) {
    unused(t);
  }

  public void next(final T t, final T next) {
    unused(t);
    unused(next);
  }

  public void prev(final T t, final T previous) {
    unused(previous);
    unused(t);
  }

  public void prolog(final T t) {
    unused(t);
  }
}
