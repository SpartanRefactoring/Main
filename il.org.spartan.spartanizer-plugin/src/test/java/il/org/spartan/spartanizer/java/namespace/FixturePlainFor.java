package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.meta.*;

/** Fixture for testing plain for loops
 * @author Yossi Gil
 * @since 2017-01-01 */
public class FixturePlainFor extends MetaFixture {
  int f(final int[] a) {
    @knows({ "f/1", "$" }) int $ = 12;
    for (@knows({ "f/1", "$", "i" }) int i = 0; i < a.length; ++i)
      for (@knows({ "f/1", "$", "i", "j" }) int j = 0; j < a.length; ++j) {
        @knows({ "f/1", "$", "i", "j", "f" }) final int f = hashCode();
        $ += f + f * i * j;
      }
    return $;
  }
}
