package il.org.spartan.utils;

import java.util.Objects;

/**
 * TODO Yossi Gil: document class
 *
 * @author Yossi Gil
 * @since 2017-03-21
 */
public class Boxer<Inner> {
  public Inner inner;

  public Boxer(final Inner inner) {
    this.inner = inner;
    if (inner == null || inner == this)
      throw new IllegalArgumentException();
  }

  @Override public int hashCode() {
    return Objects.hash(inner);
  }

  @Override public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null)
      return false;
    if (getClass() != o.getClass())
      return false;
    return Objects.equals(inner, ((Boxer<?>) o).inner);
  }
}
