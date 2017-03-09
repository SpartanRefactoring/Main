package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link PojoConstructor}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class PojoConstructorTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new PojoConstructor());
  }

  @Test public void a() {
    assert is("public class Foo{Foo(Object o){this.c = o;}}");
  }

  @Test public void b() {
    assert not("boolean foo(){ x= o;}");
  }

  @Test public void c() {
    assert is("public class Foo{@Override public Foo(Whatever o) {c = o;}}");
  }

  @Test public void f() {
    assert is("public class Foo{ public Foo(int x, int _y) {this.x = x; y = _y;}}");
  }

  @Test public void g() {
    assert not("public void setXY(int x, int _y) {this.x = x; y = _y; return this;}");
  }

  @Test public void h() {
    assert not("boolean foo(Object o){this.c = o; return this;}");
  }

  @Test public void i() {
    assert not("boolean foo(Object o){}");
  }

  @Test public void j() {
    assert is("public class Foo{ public static Foo() {    cacheEnabled = true;  }}");
  }
}
