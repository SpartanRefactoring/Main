package il.org.spartan.utils;

/** A poor man's approximation of a mutable String.
 * @author Ori Marcovitch
 * @since Oct 18, 2016 */
public final class Str {
  public String inner;

  public Str() {
    inner = null;
  }

  public Str(final Object ¢) {
    inner = ¢ + "";
  }

  public void set(final Object ¢) {
    inner = ¢ + "";
  }

  public String inner() {
    return inner;
  }

  public boolean isEmptyx() {
    return inner == null;
  }

  public boolean notEmpty() {
    return inner != null;
  }
}
