package il.org.spartan.utils;

/**
 * A poor man's approximation of a mutable boolean, which is so much more
 * convenient than {@link Boolean}
 *
 * @author Ori Marcovitch
 * @since Oct 16, 2016
 */
public final class Bool {
  public static Bool valueOf(final boolean ¢) {
    return new Bool(¢);
  }

  public boolean inner;

  public Bool() {
  }

  public Bool(final boolean b) {
    inner = b;
  }

  public Bool clear() {
    return set(false);
  }

  public boolean get() {
    return inner;
  }

  /** Function form, good substitute for auto-boxing */
  public Boolean inner() {
    return Boolean.valueOf(inner);
  }

  public Bool set() {
    return set(true);
  }

  public Bool set(final boolean ¢) {
    inner = ¢;
    return this;
  }
}
