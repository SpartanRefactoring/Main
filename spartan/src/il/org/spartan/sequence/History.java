package il.org.spartan.sequence;

import java.util.*;

import fluent.ly.*;

/** @author Yossi Gil
 * @param <T>
 * @since 8 באוק 2011 */
public class History<T> {
  private final int n;
  private final List<T> inner = new ArrayList<>();

  public History(final int n) {
    ___.positive(n);
    this.n = n;
  }

  void add(final T ¢) {
    inner.add(¢);
    while (inner.size() > n)
      inner.remove(0);
  }
}
