package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.methods.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class SetterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Setter());
  }

  @Test public void a() {
    assert is("boolean foo(Object o){this.c = o;}");
  }

  @Test public void b() {
    assert not("boolean foo(){ x= o;}");
  }

  @Test public void c() {
    assert is("@Override public int set(Whatever o) {c = o;}");
  }

  @Test public void d() {
    assert is("@Override public int set(final Whatever o) {c = o;}");
  }

  @Test public void e() {
    assert is(" public void stop() {    shouldRun = false;}");
  }

  @Test public void f() {
    assert is(" public void setXY(int x, int _y) {this.x = x; y = _y;}");
  }
}
