package il.org.spartan.spartanizer.java.namespace;

import java.io.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Fixture for testing plain for testing statements in a block.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-01 */
public class FixtureBlock extends ReflectiveTester {
  int f(@knows("ps") final int... ps) throws IOException {
    @knows({ "ps", "f/1" }) int a = ps[0] + hashCode();
    @knows({ "a", "ps", "f/1" }) int b = ps[1] + a * hashCode();
    @knows({ "a", "b", "ps", "f/1" }) int c = ps[2] + b * hashCode();
    f(a, b, c, c, c);
    return f();
  }

  int g(@knows("ps") final int... ps) throws IOException {
    for (;;) {
      @knows({ "ps", "f/1" }) int a = ps[0] + hashCode();
      @knows({ "a", "ps", "f/1" }) int b = ps[1] + a * hashCode();
      @knows({ "a", "b", "ps", "f/1" }) int c = ps[2] + b * hashCode();
      if (f(a, b, c, c, c) < 2)
        break;
    }
    return f();
  }
}