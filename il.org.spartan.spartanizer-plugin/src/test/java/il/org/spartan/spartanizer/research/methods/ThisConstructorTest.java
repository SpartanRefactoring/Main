package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link ThisConstructor}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-21 */
@SuppressWarnings("static-method")
public class ThisConstructorTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new ThisConstructor());
  }
  @Test public void a() {
    assert is("public class Foo{Foo(Object o){this(o,o);}}");
  }
  @Test public void b() {
    assert not("boolean foo(){ x= o;}");
  }
  @Test public void c() {
    assert not("public class Foo{@Override public Foo(Whatever o) {c = o;}}");
  }
  @Test public void f() {
    assert is("public class Foo{ public Foo(int x, int _y) {this();}}");
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
}
