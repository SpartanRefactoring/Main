/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package fluent.ly;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @param <C> JD
 * @since 2016 */
public interface accumulate<T, C extends Collection<T>> {
  /** @param <T> JD
   * @param <C> JD
   * @param c JD */
  static <T, C extends Collection<T>> accumulate<T, C> to(final C c) {
    return new accumulate<>() {
      @Override public accumulate<T, C> add(final @NotNull T ¢) {
        if (¢ == null)
          return this;
        c.add(¢);
        return this;
      }
      @Override public C elements() {
        return c;
      }
    };
  }
  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> add(final Iterable<? extends T> ts) {
    for (final T ¢ : ts)
      if (¢ != null)
        add(¢);
    return this;
  }
  /** @param t JD
   * @return <code><b>this</b></code> */
  accumulate<T, C> add(T t);
  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> add(@SuppressWarnings("unchecked") final T... ts) {
    if (ts != null)
      for (final T ¢ : ts)
        if (¢ != null)
          add(¢);
    return this;
  }
  /** @param ts JD
   * @return <code><b>this</b></code> */
  default accumulate<T, C> addAll(final Iterable<? extends T> ts) {
    if (ts != null)
      for (final T ¢ : ts)
        if (¢ != null)
          add(¢);
    return this;
  }
  /** @param tss JD
   * @return <code><b>this</b></code> */
  @SuppressWarnings("unchecked") default accumulate<T, C> addAll(final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ¢ : tss)
      addAll(¢);
    return this;
  }
  C elements();
}
