/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import java.util.*;

import org.eclipse.jdt.annotation.*;

public
interface accumulate<T, C extends Collection<T>> {
  default accumulate<T, C> add(final Iterable<? extends T> ts) {
    for (@Nullable final T t : ts)
      if (t != null)
        add(t);
    return this;
  }
  accumulate<T, C> add(final @Nullable T t);
  default accumulate<T, C> add(final @Nullable T @Nullable... ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }
  default accumulate<T, C> addAll(final @Nullable Iterable<? extends T> ts) {
    if (ts != null)
      for (@Nullable final T t : ts)
        if (t != null)
          add(t);
    return this;
  }
  default accumulate<T, C> addAll(final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ts : tss)
      addAll(ts);
    return this;
  }
  C elements();
}