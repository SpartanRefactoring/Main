package il.org.spartan.collections;

import java.util.*;

/** Set operations implemented as a state-less class, with just static
 * methods. */
public enum Sets {
  ;
  public static <T, S extends T> Collection<T> addAll(final Collection<T> $, final Iterable<S> src) {
    for (final S ¢ : src)
      $.add(¢);
    return $;
  }
  public static <T extends Collection<E>, E> T addAll(final T $, final E[] src) {
    for (final E ¢ : src)
      $.add(¢);
    return $;
  }
  public static <E> Set<E> intersection(final Collection<E> lhs, final Collection<E> rhs) {
    final Set<E> $ = new HashSet<>(lhs);
    $.retainAll(rhs);
    return $;
  }
  public static <E> Set<E> union(final Collection<E> lhs, final Collection<E> rhs) {
    final Set<E> $ = new HashSet<>(lhs);
    $.addAll(rhs);
    return $;
  }
}
