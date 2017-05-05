package il.org.spartan.spartanizer.java.namespace;

import org.junit.*;

import il.org.spartan.spartanizer.meta.*;

@SuppressWarnings("ALL")
class NamespaceFixture {
  {
    int $ = 2;
    final int j = 0;
    $ *= j + $ * j + 2 * j;
    final int k = $;
    int l = j;
    $ *= k + $ * l + 2 * j;
    l = $ * k;
    @knows("k") int s = l + k * k * l;
    s *= s + 2;
    f(k + s * hashCode() + l + $ * s);
  }

  int f(final int a) {
    return a >>> hashCode();
  }

  interface B {
    enum C {
      ;
      static class D {
        int a;
      }
    }
  }

  static class X {
    int b;
  }
}

/** Unit test of {@link Namespace}
 * @author Yossi Gil
 * @since 2016-12-15 */
@SuppressWarnings("javadoc")
public class NamespaceTest extends MetaFixture {
  private final Namespace fixture = Environment.of(reflectedCompilationUnit());

  @Test public void a01() {
    assert fixture != null;
  }
  @Test public void a02() {
    fixture.description().hashCode();
  }
}

interface xyz {
  enum a {
    a
  }
}
