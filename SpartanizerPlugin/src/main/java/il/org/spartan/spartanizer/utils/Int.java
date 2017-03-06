package il.org.spartan.spartanizer.utils;

import il.org.spartan.utils.*;

/** A poor man's approximation of a mutable int, which is so much more
 * convenient than {@link Integer}
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Sep 12, 2016 */
public final class Int {
  public int inner;

  public Int() {
    ___.______unused();
  }

  public Int(final int inner) {
    this.inner = inner;
  }

  /** Function form, good substitute for auto-boxing */
  public Integer inner() {
    return Integer.valueOf(inner);
  }

  /** @param ¢ JD
   * @return */
  public static Int valueOf(final int ¢) {
    final Int $ = new Int();
    $.inner = ¢;
    return $;
  }

  public void step() {
    ++inner;
  }

  public int get() {
    return inner;
  }

  public void add(final int value) {
    inner += value;
  }

  public void set(final int inner) {
    this.inner = inner;
  }

  @Override public String toString() {
    return inner + "";
  }

  public int next() {
    return ++inner;
  }
}
