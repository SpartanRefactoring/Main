package il.org.spartan.utils;

import org.jetbrains.annotations.Nullable;

/** A poor man's approximation of a mutable String.
 * @author Ori Marcovitch
 * @since Oct 18, 2016 */
public final class Str {
  @Nullable public String inner;

  public Str() {
    inner = null;
  }

  public Str(final Object ¢) {
    inner = ¢ + "";
  }

  public void set(final Object ¢) {
    inner = ¢ + "";
  }

  @Nullable public String inner() {
    return inner;
  }

  public boolean isEmptyx() {
    return inner == null;
  }

  public boolean notEmpty() {
    return inner != null;
  }
}
