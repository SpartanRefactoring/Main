package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.characteristics.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class FluentSetterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new FluentSetter());
  }

  @Test public void a() {
    assert is("boolean foo(Object o){this.c = o; return this;}");
  }

  @Test public void b() {
    assert not("boolean foo(Object o){this.c = o;}");
  }

  @Test public void c() {
    assert is("@Override public int set(Whatever o) {c = o; return this;}");
  }

  @Test public void d() {
    assert is("@Override public int set(final Whatever o) {c = o; return this;}");
  }
}
