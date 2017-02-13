package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link Let}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
@SuppressWarnings("static-method")
public class LetTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Let());
  }

  @Test public void a() {
    assert not("boolean foo(){A x = 123; return bar(x,x);}");
  }

  @Test public void b() {
    assert not("boolean foo(){A x = 123; bar(x,x);}");
  }

  @Test public void c() {
    assert is("boolean foo(){A x = foo(); return bar(x,x);}");
  }

  @Test public void d() {
    assert is("boolean foo(){A x = foo(); bar(x,x);}");
  }
}
