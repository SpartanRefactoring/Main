package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.SuperConstructor;

/** Tests {@link SuperConstructorTest}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-10 */
@SuppressWarnings("static-method")
public class SuperConstructorTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new SuperConstructor());
  }
  @Test public void a() {
    assert is("public class Foo{Foo(Object o){super(o);}}");
  }
  @Test public void b() {
    assert not("boolean foo(){ x= o;}");
  }
  @Test public void c() {
    assert is("public class Foo{public Foo(int a) {super(a);}}");
  }
  @Test public void d() {
    assert not("public class Foo{ public Foo(int x, int _y) {super(z);}}");
  }
  @Test public void e() {
    assert is("public class Foo{ public Foo(int x, int _y) {super(_y,x);}}");
  }
  @Test public void f() {
    assert is("public class Foo{ public Foo(int x, int _y) {super(x);}}");
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
    assert not("public class Foo{ public  Foo() { super(); super(); }}");
  }
  @Test public void k() {
    assert is("public class Foo{ public Foo(int a, object b) { super(a,b);  }}");
  }
  @Test public void l() {
    assert not("public class Foo{ public Foo() {}}");
  }
  @Test public void m() {
    assert not("public class Foo{ public Foo(Object o) {}}");
  }
}
