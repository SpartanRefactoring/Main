package il.org.spartan.utils;

import fluent.ly.*;

/** A poor man's approximation of a mutable int, which is so much more
 * convenient than {@link Integer}
 * @author Yossi Gil
 * @since Sep 12, 2016 */
public final class Int {
  public int inner;

  public Int() {
    Object[] ____ = {};
    forget.em(____);
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

  public void add(final Int other) {
    inner += other.get();
  }

  public void clear() {
    inner = 0;
  }
}
