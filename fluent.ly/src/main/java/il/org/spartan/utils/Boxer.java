package il.org.spartan.utils;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-21 */
public class Boxer<Inner> {
  public Inner inner;

  public Boxer(final Inner inner) {
    this.inner = inner;
    if (inner == null || inner == this)
      throw new IllegalArgumentException();
  }

  @Override public boolean equals(final Object ¢) {
    if (¢ == this)
      return true;
    if (¢ == null || getClass() != ¢.getClass())
      return false;
    @SuppressWarnings("unchecked") final Boxer<Inner> $ = (Boxer<Inner>) ¢;
    return equals($);
  }

  protected boolean equals(final Boxer<Inner> other) {
    if (inner == null) {
      if (other.inner != null)
        return false;
    } else if (!inner.equals(other.inner))
      return false;
    return true;
  }

  @Override public int hashCode() {
    return 31 + (inner == null ? 0 : inner.hashCode());
  }
}
