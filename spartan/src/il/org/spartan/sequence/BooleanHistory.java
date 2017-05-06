package il.org.spartan.sequence;

import org.eclipse.jdt.annotation.*;

import fluent.ly.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
public class BooleanHistory {
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
      $ += as.bit(b == bs[(last + bs.length - 1 - ¢) % bs.length]);
    return $;
  }
  public int size() {
    return size;
  }
}
