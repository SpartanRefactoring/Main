package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable String.
 * @author Ori Marcovitch
 * @year 2016 */
public final class Str {
  public String inner;

  /** Function form, good substitute for auto-boxing */
  public String inner() {
    return inner;
  }
}