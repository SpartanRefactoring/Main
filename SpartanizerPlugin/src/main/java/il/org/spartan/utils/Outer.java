package il.org.spartan.utils;

import org.jetbrains.annotations.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-21 */
public abstract class Outer<Inner> {
  @Nullable public final Inner inner;

  public Outer(@Nullable final Inner inner) {
    this.inner = inner;
    if (inner == null || inner == this)
      throw new IllegalArgumentException();
  }

  @Override public boolean equals(@Nullable final Object ¢) {
    if (¢ == this)
      return true;
    if (¢ == null || getClass() != ¢.getClass())
      return false;
    @Nullable @SuppressWarnings("unchecked") final Outer<Inner> $ = (Outer<Inner>) ¢;
    return equals($);
  }

  protected boolean equals(@NotNull final Outer<Inner> other) {
    if (inner == null) {
      if (other.inner != null)
        return false;
    } else if (!inner.equals(other.inner))
      return false;
    return true;
  }

  @Override public int hashCode() {
    return 31 * 1 + (inner == null ? 0 : inner.hashCode());
  }
}
