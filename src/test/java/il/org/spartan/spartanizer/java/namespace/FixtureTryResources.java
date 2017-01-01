package il.org.spartan.spartanizer.java.namespace;

import java.io.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Fixture for testing plain for testing resources in try statement
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-01 */
public class FixtureTryResources extends ReflectiveTester {
  @knows("f/0") int f() throws FileNotFoundException, IOException {
    try (@knows("$") FileReader $ = new FileReader(toString())) {
      try (@knows({ "b", "$" }) FileReader b = new FileReader(toString())) {
        if (f() == 3)
          return $.hashCode();
        try (@knows({ "c", "b", "$" }) FileReader c = new FileReader(toString())) {
          try (@knows({ "f/0", "c", "b", "$", "d" }) FileReader d = new FileReader(toString())) {
            if (f() == 3)
              return c.hashCode();
          }
          @knows("$") int a = hashCode() / 2;
          if (f() == a)
            return a * c.hashCode() + $.hashCode();
        }
      }
    }
    @foreign("$") int x = hashCode() >>> 3;
    @knows("$") int $ = 100 *hashCode() >>> 3;
    return f() == x ? x + $ : 2 * hashCode();
  }
}
