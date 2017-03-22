package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.meta.*;
import org.jetbrains.annotations.NotNull;

/** Fixture for testing plain for loops
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-01 */
public class FixturePlainFor extends MetaFixture {
  int f(@NotNull final int[] a) {
    @knows({ "f/1", "$" }) int $ = 12;
    for (@knows({ "f/1", "$", "i" }) int i = 0; i < a.length; ++i)
      for (@knows({ "f/1", "$", "i", "j" }) int j = 0; j < a.length; ++j) {
        @knows({ "f/1", "$", "i", "j", "f" }) final int f = hashCode();
        $ += f * i * j + f;
      }
    return $;
  }
}
