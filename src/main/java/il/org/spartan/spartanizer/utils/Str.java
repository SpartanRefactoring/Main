package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable String.
 * @author Ori Marcovitch
 * @year 2016 */
public final class Str {
  private String inner;

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