/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import java.util.*;

import org.eclipse.jdt.annotation.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @param <C> JD
 * @since 2016 */
public interface accumulate<T, C extends Collection<T>> {
  /** @param <T> JD
   * @param <C> JD
   * @param c JD
   * @return TODO document return type */
  static <T, C extends Collection<T>> accumulate<T, C> to(final C c) {
    return new accumulate<T, C>() {
      @Override public accumulate<T, C> add(final @Nullable T t) {
        if (t == null)
          return this;
        c.add(t);
        return this;
      }

      @Override public C elements() {
        return c;
      }
    };
  }

  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> add(final Iterable<? extends @Nullable T> ts) {
    for (@Nullable final T t : ts)
      if (t != null)
        add(t);
    return this;
  }

  /** @param t JD
   * @return <code><b>this</b></code> */
  accumulate<T, C> add(final @Nullable T t);

  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> add(@SuppressWarnings("unchecked") final @Nullable T @Nullable... ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }

  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> addAll(final @Nullable Iterable<? extends T> ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }

  /** @param tss JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> addAll(final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ts : tss)
      addAll(ts);
    return this;
  }

  /** @return TODO document return type */
  C elements();
}
