package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

/** A poor man's approximation of a mutable int, which is so much more
 * convenient than {@link Integer}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
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
  @NotNull
  public static Int valueOf(final int ¢) {
    @NotNull final Int $ = new Int();
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

  @NotNull
  @Override public String toString() {
    return inner + "";
  }

  public int next() {
    return ++inner;
  }
}
