package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable boolean, which is so much more
 * convenient than {@link Integer}
 * @author Ori Marcovitch
 * @year 2016 */
public final class Bool {
  public boolean inner;

  /** Function form, good substitute for auto-boxing */
  public Boolean inner() {
    return Boolean.valueOf(inner);
  }
}