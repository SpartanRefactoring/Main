/**
 *
 */
package il.org.spartan.sequence;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
public class BooleanHistory {
  @NotNull
  private final boolean bs[];
  private int size;
  private int last;

  public BooleanHistory(final int n) {
    ___.positive(n);
    bs = new boolean[n];
    size = 0;
  }

  public void add(final boolean ¢) {
    bs[last++] = ¢;
    if (last == bs.length)
      last = 0;
    if (size < bs.length)
      ++size;
  }

  public int count(final boolean b) {
    int $ = 0;
    for (int ¢ = 0; ¢ < size; ++¢)
      $ += as.bit(b == bs[(bs.length + last - 1 - ¢) % bs.length]);
    return $;
  }

  public int size() {
    return size;
  }
}
