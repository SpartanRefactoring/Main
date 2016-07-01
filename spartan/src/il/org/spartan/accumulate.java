/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import java.util.*;

import org.eclipse.jdt.annotation.*;

/**
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T>
 * @param <C>
 * @since 2016
 */
public interface accumulate<T, C extends Collection<T>> {
  /**
   * @param ts
   * @return <code><b>this</b></code>
   */
  default accumulate<T, C> add(final Iterable<? extends @Nullable T> ts) {
    for (@Nullable final T t : ts)
      if (t != null)
        add(t);
    return this;
  }
  /**
   * @param t JD
   * @return
   */
  accumulate<T, C> add(final @Nullable T t);
  /**
   * @param ts
   * @return
   */
  default accumulate<T, C> add(final @Nullable T @Nullable... ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }
  /**
   * @param ts
   * @return
   */
  default accumulate<T, C> addAll(final @Nullable Iterable<? extends T> ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }
  /**
   * @param tss
   * @return
   */
  default accumulate<T, C> addAll(final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ts : tss)
      addAll(ts);
    return this;
  }
  C elements();
  public static <T, C extends Collection<T>> accumulate<T, C> to(final C c) {
    return new accumulate<T, C>() {
      @Override public accumulate<T, C> add(final @Nullable T t) {
        if (t == null)
          return this;
        assert t != null;
        c.add(t);
        return this;
      }
      @Override public C elements() {
        return c;
      }
    };
  }
}