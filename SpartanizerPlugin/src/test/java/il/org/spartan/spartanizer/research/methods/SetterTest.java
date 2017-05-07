package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link Setter}
 * @author Ori Marcovitch */
// TODO: Ori Marcovitch
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
  @Test public void g() {
    assert not(" public void setXY(int x, int _y) {this.x = x; y = _y; return this;}");
  }
  @Test public void h() {
    assert not("boolean foo(Object o){this.c = o; return this;}");
  }
  @Test public void i() {
    assert not("boolean foo(Object o){}");
  }
  @Test public void j() {
    assert is("public static void enableCache() {    cacheEnabled = true;  }");
  }
}
