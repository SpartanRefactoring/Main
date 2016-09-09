package il.org.spartan.collections;

import java.util.*;

/** Set operations implemented as a state-less class, with just static
 * methods. */
public enum Sets {
  ;
  public static <T, S extends T> Collection<T> addAll(final Collection<T> dest, final Iterable<S> src) {
    for (final S s : src)
      dest.add(s);
    return dest;
  }

  public static <T extends Collection<E>, E> T addAll(final T dest, final E[] src) {
    for (final E e : src)
      dest.add(e);
    return dest;
  }

  public static <E> Set<E> intersection(final Collection<E> lhs, final Collection<E> rhs) {
    final Set<E> r = new HashSet<>(lhs);
    r.retainAll(rhs);
    return r;
  }

  public static <E> Set<E> union(final Collection<E> lhs, final Collection<E> rhs) {
    final Set<E> r = new HashSet<>(lhs);
    r.addAll(rhs);
    return r;
  }
}
