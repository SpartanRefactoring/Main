package fluent.ly;

public interface count {
  static <T> int of(final Iterable<T> ts) {
    int $ = 0;
    for (@SuppressWarnings("unused") final T __ : ts)
      ++$;
    return $;
  }
  /** Counts the number of items in an {@link Iterable}.
   * @param <T> some arbitrary type
   * @param ts some iterable over items whose type is the type parameter
   * @return number of items the given iterable yields. */
  static <T> int notNull(final Iterable<T> ts) {
    int $ = 0;
    if (ts != null)
      for (final T ¢ : ts)
        $ += as.bit(¢ != null);
    return $;
  }
}
