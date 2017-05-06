package il.org.spartan.utils;

/** Reducer that concatenate strings
 * @author Yossi Gil
 * @since 2017-03-19 */
public class CountingReduce extends Reduce<Integer> {
  @Override @SuppressWarnings("boxing") public final Integer reduce(final Integer i1, final Integer i2) {
    return i1 + i2;
  }
  @Override @SuppressWarnings("boxing") public final Integer reduce() {
    return 0;
  }
}
