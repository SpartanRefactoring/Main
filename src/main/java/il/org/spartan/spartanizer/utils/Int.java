package il.org.spartan.spartanizer.utils;

/** A poor man's approximation of a mutable int, which is so much more
 * convenient than {@link Integer}
 * @author Yossi Gil
 * @year 2016 */
public final class Int {
  public int inner;

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
}