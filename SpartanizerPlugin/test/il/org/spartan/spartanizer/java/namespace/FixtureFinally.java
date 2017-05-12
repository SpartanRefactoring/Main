package il.org.spartan.spartanizer.java.namespace;

import java.io.*;

import il.org.spartan.spartanizer.meta.*;

/** Fixture for testing plain for testing resources in try statement
 * @author Yossi Gil
 * @since 2017-01-01 */
@SuppressWarnings({ "ALL", "TooBroadScope", "all" })
public class FixtureFinally extends MetaFixture {
  int simple(@knows("p") final int p) throws IOException {
    final int x = 2 * p;
    try (FileReader r = new FileReader(toString())) {
      r.read();
      if (simple(2 * p * p) < 0)
        return simple(hashCode());
    } finally {
      @foreign("r") final int a = hashCode() * hashCode();
      @knows({ "a", "p" }) final int r = hashCode();
      // noinspection InconsistentLineSeparators
      if (simple(3 * a * r * x) < 0)
        simple(a * p * r);
    }
    return simple(hashCode());
  }
}