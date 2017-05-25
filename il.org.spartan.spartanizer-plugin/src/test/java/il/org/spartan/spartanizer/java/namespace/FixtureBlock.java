package il.org.spartan.spartanizer.java.namespace;

import java.io.*;

import il.org.spartan.spartanizer.meta.*;

/** Fixture for testing plain for testing statements in a block.
 * @author Yossi Gil
 * @since 2017-01-01 */
public class FixtureBlock extends MetaFixture {
  @SuppressWarnings("InfiniteRecursion") private int f(@knows("ps") final int... ps) throws IOException {
    @knows({ "ps", "f/1" }) final int a = ps[0] + hashCode();
    @knows({ "a", "ps", "f/1" }) final int b = ps[1] + a * hashCode();
    @knows({ "a", "b", "ps", "f/1" }) final int c = ps[2] + b * hashCode();
    f(a, b, c, c, c);
    return f();
  }
  /** [[SuppressWarningsSpartan]] */
  int g(@knows("ps") final int... ps) throws IOException {
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      @knows({ "ps", "f/1" }) final int a = ps[0] + hashCode();
      @knows({ "a", "ps", "f/1" }) final int b = ps[1] + a * hashCode();
      @knows({ "a", "b", "ps", "f/1" }) final int c = ps[2] + b * hashCode();
      if (f(a, b, c, c, c) < 2)
        break;
    }
    return f();
  }
}