package fluent.ly;

public interface count {
  static <T> int of(Iterable<T> ts) {
    int $ = 0;
    for (@SuppressWarnings("unused") T __ : ts)
      ++$;
    return $;
  }
}
