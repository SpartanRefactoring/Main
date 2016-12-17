package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable boolean, which is so much more
 * convenient than {@link Boolean}
 * @author Ori Marcovitch
 * @year 2016 */
public final class Bool {
  public boolean inner;

  public Bool() {}

  private Bool(final boolean b) {
    inner = b;
  }

  /** Function form, good substitute for auto-boxing */
  public Boolean inner() {
    return Boolean.valueOf(inner);
  }

  public static Bool valueOf(final boolean ¢) {
    return new Bool(¢);
  }
}