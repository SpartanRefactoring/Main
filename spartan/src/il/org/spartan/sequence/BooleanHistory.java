/**
 *
 */
package il.org.spartan.sequence;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
public class BooleanHistory {
  private final boolean bs[];
  private int size = 0;
  private int last = 0;

  public BooleanHistory(final int n) {
    ____.positive(n);
    bs = new boolean[n];
    size = 0;
  }

  public void add(final boolean b) {
    bs[last++] = b;
    if (last == bs.length)
      last = 0;
    if (size < bs.length)
      size++;
  }

  public int count(final boolean b) {
    int $ = 0;
    for (int i = 0; i < size; i++)
      $ += As.binary(b == bs[(bs.length + last - 1 - i) % bs.length]);
    return $;
  }

  public int size() {
    return size;
  }
}
