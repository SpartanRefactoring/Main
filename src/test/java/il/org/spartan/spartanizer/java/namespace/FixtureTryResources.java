package il.org.spartan.spartanizer.java.namespace;

import java.io.*;

import il.org.spartan.spartanizer.meta.*;
import org.jetbrains.annotations.NotNull;

/** Fixture for testing plain for testing resources in try statement
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-01 */
@SuppressWarnings("InfiniteRecursion")
public class FixtureTryResources extends MetaFixture {
  @knows("f/0") int f() throws IOException {
    try (@NotNull @knows("$") FileReader $ = new FileReader(toString())) {
      try (@NotNull @knows({ "b", "$" }) FileReader b = new FileReader(toString())) {
        if (f() == 3)
          return $.hashCode();
        try (@NotNull @knows({ "c", "b", "$" }) FileReader c = new FileReader(toString())) {
          try (@NotNull @knows({ "f/0", "c", "b", "$", "d" }) FileReader d = new FileReader(toString())) {
            if (f() == 3)
              return c.hashCode();
          }
          @knows("$") final int a = hashCode() / 2;
          if (f() == a)
            return a * c.hashCode() + $.hashCode();
        }
      }
    }
    @foreign("$") final int x = hashCode() >>> 3;
    @knows("$") final int $ = 100 * hashCode() >>> 3;
    return f() == x ? x + $ : 2 * hashCode();
  }
}
