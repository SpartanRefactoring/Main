package il.org.spartan.collections;

import java.util.*;

import org.jetbrains.annotations.*;

/** Set operations implemented as a state-less class, with just static
 * methods. */
public enum Sets {
  ;
  @NotNull public static <T, S extends T> Collection<T> addAll(@NotNull final Collection<T> $, @NotNull final Iterable<S> src) {
    for (final S ¢ : src)
      $.add(¢);
    return $;
  }
  @NotNull public static <T extends Collection<E>, E> T addAll(@NotNull final T $, @NotNull final E[] src) {
    for (final E ¢ : src)
      $.add(¢);
    return $;
  }
  @NotNull public static <E> Set<E> intersection(@NotNull final Collection<E> lhs, @NotNull final Collection<E> rhs) {
    @NotNull final Set<E> $ = new HashSet<>(lhs);
    $.retainAll(rhs);
    return $;
  }
  @NotNull public static <E> Set<E> union(@NotNull final Collection<E> lhs, @NotNull final Collection<E> rhs) {
    @NotNull final Set<E> $ = new HashSet<>(lhs);
    $.addAll(rhs);
    return $;
  }
}
