package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.characteristics.*;

/** @author Ori Marcovitch
 * @since 2016 */
@Ignore // TODO: Ori Marco
@SuppressWarnings("static-method")
public class CascadingSetterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Cascading.Setter());
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

  @Test public void e() {
    assert is(" public Whatever setXY(int x, int _y) {this.x = x; y = _y; return this;}");
  }

  @Test public void f() {
    assert not(" public void setXY(int x, int _y) {this.x = x; y = _y;}");
  }

  @Test public void g() {
    assert not(" public Whatever setXY(int x, int _y) {this.x = x; y = _y; return o;}");
  }

  @Test public void h() {
    assert not(" public Whatever setXY() {return this;}");
  }
}
