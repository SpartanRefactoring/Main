/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package nano.ly;

import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

/** @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @param <T> JD
 * @param <C> JD
 * @since 2016 */
public interface accumulate<T, C extends Collection<T>> {
  /** @param <T> JD
   * @param <C> JD
   * @param c JD */
  @Nullable static <T, C extends Collection<T>> accumulate<T, C> to(@NotNull final C c) {
    return new accumulate<T, C>() {
      @Override @NotNull public accumulate<T, C> add(final @Nullable T ¢) {
        if (¢ == null)
          return this;
        c.add(¢);
        return this;
      }

      @Override @NotNull public C elements() {
        return c;
      }
    };
  }

  /** @param ts JD
   * @return <code><b>this</b></code> */
  @NotNull default accumulate<T, C> add(@NotNull final Iterable<? extends @Nullable T> ts) {
    for (@Nullable final T ¢ : ts)
      if (¢ != null)
        add(¢);
    return this;
  }

  /** @param t JD
   * @return <code><b>this</b></code> */
  @NotNull accumulate<T, C> add(@Nullable T t);

  /** @param ts JD
   * @return <code><b>this</b></code> */
  @NotNull default accumulate<T, C> add(@SuppressWarnings("unchecked") final @Nullable T @Nullable... ts) {
    if (ts != null)
      for (@Nullable final T ¢ : ts)
        if (¢ != null)
          add(¢);
    return this;
  }

  /** @param ts JD
   * @return <code><b>this</b></code> */
  @NotNull default accumulate<T, C> addAll(final @Nullable Iterable<? extends T> ts) {
    if (ts != null)
      for (@Nullable final T ¢ : ts)
        if (¢ != null)
          add(¢);
    return this;
  }

  /** @param tss JD
   * @return <code><b>this</b></code> */
  @NotNull default accumulate<T, C> addAll(@NotNull final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ¢ : tss)
      addAll(¢);
    return this;
  }

  @NotNull C elements();
}
