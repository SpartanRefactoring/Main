package il.org.spartan.spartanizer.research.characteristics;

import org.junit.*;

import il.org.spartan.spartanizer.research.methods.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;

/** Tests {@link MyArguments}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class MyArgumentsTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new MyArguments());
  }
  @Test public void a() {
    assert is("boolean foo(int a, int b){return foo(a,b);}");
  }
  @Test public void b() {
    assert is("boolean foo(int a, int b){return foo(a,b) +  bar(a,b);}");
  }
  @Test public void c() {
    assert is("boolean foo(int a, int b){return foo(a,b,c);}");
  }
  @Test public void d() {
    assert is("boolean foo(int a, int b){return foo(c,a,b);}");
  }
  @Test public void e() {
    assert is("boolean foo(int a, int b){return foo(c,a,b,d);}");
  }
  @Test public void f() {
    assert is("boolean foo(int a, int b){return foo(c,a,b,d) + foo(c,a,b,d);}");
  }
  @Test public void g() {
    assert is("boolean foo(int a, int b){return foo(a,b,d) + foo(c,a,b);}");
  }
  @Test public void h() {
    assert is("boolean foo(int a, int b){return foo(a,b,bar(c,a,b));}");
  }
  @Test public void i() {
    assert not("boolean foo(int a){return foo(a,b);}");
  }
  @Test public void j() {
    assert not("boolean foo(){return foo(a,b);}");
  }
  @Test public void k() {
    assert not("boolean foo(int a, int b){}");
  }
  @Test public void l() {
    assert not("boolean foo(){}");
  }
  @Test public void m() {
    assert not("boolean foo(int a, int b, int c){bar(a,b,d,c);}");
  }
  @Test public void n() {
    assert not("boolean foo(int a, int b, int c){bar(a,b,c2);}");
  }
  @Test public void o() {
    assert not("boolean foo(int a, int b){if(x) return foo(c,a,b,d); else foo(c,a,b,d).f(a);}");
  }
  @Test public void p() {
    assert not("boolean foo(int a, int b){this.a = a; this.b = b;}");
  }
  @Test public void q() {
    assert not("boolean foo(int a, int b){foo();}");
  }
}
