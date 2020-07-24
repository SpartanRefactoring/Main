package il.org.spartan.spartanizer.java.namespace;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import il.org.spartan.spartanizer.meta.MetaFixture;

/** Fixture for testing plain for testing resources in the catch block of a try
 * statement
 * @author Yossi Gil
 * @since 2017-01-01 */
@SuppressWarnings("InfiniteRecursion")
public class FixtureCatchBlock extends MetaFixture {
  @knows("f/0") private int f() {
    try (@knows("fileReader") FileReader ret = new FileReader(toString())) {
      try (@knows({ "b", "fileReader" }) FileReader b = new FileReader(toString())) {
        if (f() == 3)
          return ret.hashCode();
        try (@knows({ "c", "b", "fileReader" }) FileReader c = new FileReader(toString())) {
          try (@knows({ "f/0", "c", "b", "fileReader", "d" }) FileReader d = new FileReader(toString())) {
            if (f() == 3)
              return c.hashCode();
          } catch (@foreign("d") @knows({ "y", "fileReader" }) final IOException y) {
            y.printStackTrace();
            @foreign({ "x", "d" }) final int a = hashCode() * y.hashCode();
            @knows({ "a", "y", "$" }) final int $ = a * a;
            f($ + $ * a + y.hashCode());
            y.printStackTrace();
            return 12;
          }
          if (f() == 3)
            return c.hashCode();
        } catch (@foreign("d") @knows("x") final IOException x) {
          for (@foreign("resourceInTry") @knows({ "x", "water", "wine" }) @ScopeSize(3) @for¢ int water = new Object() {
            @Override public int hashCode() {
              return super.hashCode();
            }
          }.hashCode(), wine = water;wine + water < 10; --water) {
            @knows({ "water", "x", "fig" }) @local @ScopeSize(2) final int fig = 2 * water + hashCode();
            f(fig * fig + water * hashCode());
          }
          @foreign("$") final int a = hashCode() * x.hashCode();
          @knows("a") final int z = a * a;
          f(z + a * z + x.hashCode());
          return 12;
        }
      }
      return ret.hashCode();
    } catch (@foreign("$") final IOException x) {
      @foreign("$") final int a = hashCode() * x.hashCode();
      @knows("a") final int y = a * a;
      f(y + a * y + x.hashCode());
    } finally {
      @foreign("$") final int a = hashCode() * hashCode();
      @knows("a") final int z = a * a + hashCode();
      f(z + a * z + hashCode());
    }
    return 3;
  }
  private int f(final int f) {
    return f(f);
  }
  int f(final Object f) {
    return f(f);
  }
  void simple() {
    try (FileReader r = new FileReader(toString())) {
      r.read();
    } catch (final FileNotFoundException x) {
      @foreign("r") final int a1 = hashCode() * hashCode() * x.hashCode();
      f(a1 * a1 * a1);
    } catch (final IOException x) {
      @knows({ "a2", "x" }) @foreign("r") final int a2 = hashCode() * hashCode();
      f(a2 * x.hashCode());
      @knows({ "a2", "x" }) final int r = hashCode();
      f(r + r * a2 * a2);
    } finally {
      @foreign("r") final int a = hashCode() * hashCode();
      @knows("a") final int r = hashCode();
      f(r + a * a * r);
    }
  }
}
