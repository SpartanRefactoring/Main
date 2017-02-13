package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable boolean, which is so much more
 * convenient than {@link Boolean}
 * @author Ori Marcovitch
 * @since Oct 16, 2016 */
public final class Bool {
  public boolean inner;

  public Bool() {}

  @SuppressWarnings("BooleanParameter") public Bool(final boolean b) {
    inner = b;
  }

  public boolean get() {
    return inner;
  }

  Bool set() {
    return set(true);
  }

  private Bool set(final boolean ¢) {
    inner = ¢;
    return this;
  }

  /** Function form, good substitute for auto-boxing */
  public Boolean inner() {
    return Boolean.valueOf(inner);
  }

  public static Bool valueOf(final boolean ¢) {
    return new Bool(¢);
  }

  public Bool clear() {
    return set(false);
  }
}
