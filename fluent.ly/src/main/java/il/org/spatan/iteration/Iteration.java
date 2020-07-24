package il.org.spatan.iteration;

import fluent.ly.forget;

/**
 * @param <T> Type over which we shall iterate * @author Yossi Gil
 * @since 01/05/2011
 */
public abstract class Iteration<T> {
  public void at(final T ¢) {
    forget.em(¢);
  }

  public void epilog(final T ¢) {
    forget.em(¢);
  }

  public void next(final T t, final T next) {
    forget.em(t);
    forget.em(next);
  }

  public void prev(final T t, final T previous) {
    forget.em(previous);
    forget.em(t);
  }

  public void prolog(final T ¢) {
    forget.em(¢);
  }
}
