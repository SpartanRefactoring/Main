package il.org.spartan.utils;

import org.jetbrains.annotations.*;

/** A poor man's approximation of a mutable boolean, which is so much more
 * convenient than {@link Boolean}
 * @author Ori Marcovitch
 * @since Oct 16, 2016 */
public final class Bool {
  @NotNull public static Bool valueOf(final boolean ¢) {
    return new Bool(¢);
  }

  public boolean inner;

  public Bool() {}

  @SuppressWarnings("BooleanParameter") public Bool(final boolean b) {
    inner = b;
  }

  @NotNull public Bool clear() {
    return set(false);
  }

  public boolean get() {
    return inner;
  }

  /** Function form, good substitute for auto-boxing */
  public Boolean inner() {
    return Boolean.valueOf(inner);
  }

  @NotNull public Bool set() {
    return set(true);
  }

  @NotNull public Bool set(final boolean ¢) {
    inner = ¢;
    return this;
  }
}
