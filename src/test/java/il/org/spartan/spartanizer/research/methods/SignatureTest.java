package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link Empty}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
@SuppressWarnings("static-method")
public class SignatureTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Signature());
  }

  @Test public void a() {
    assert not("boolean foo(){A x = 123; return bar(x,x);}");
  }

  @Test public void b() {
    assert not("boolean foo(){}");
  }

  @Test public void c() {
    assert is("boolean foo();");
  }

  @Test public void d() {
    assert is("void foo(int a, int b);");
  }

  @Test public void e() {
    assert is("public static void foo(int a, int b);");
  }
}
