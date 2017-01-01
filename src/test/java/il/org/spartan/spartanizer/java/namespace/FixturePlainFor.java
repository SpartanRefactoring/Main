package il.org.spartan.spartanizer.java.namespace;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Fixture for testing plain for loops
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-01 */
public class FixturePlainFor extends ReflectiveTester {
  int f(int a[]) {
    @knows({ "f/1", "$" }) int $ = 12;
    for (@knows({ "f/1", "$", "i" }) int i = 0; i < a.length; ++i)
      for (@knows({ "f/1", "$", "i", "j" }) int j = 0; j < a.length; ++j) {
        @knows({ "f/1", "$", "i", "j", "f" }) int f = hashCode();
        $ += f * i * j + f;
      }
    return $;
  }
}
