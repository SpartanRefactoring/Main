package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link DefaultValue}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class DefaultValueTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new DefaultValue());
  }

  @Test public void a() {
    assert is("int foo(){return 0;}");
  }

  @Test public void b() {
    assert is("int foo(){return 0L;}");
  }

  @Test public void c() {
    assert is("int foo(){return;}");
  }

  @Test public void d() {
    assert is("int foo(){return false;}");
  }

  @Test public void e() {
    assert is("int foo(){return null;}");
  }

  @Test public void f() {
    assert is("int foo(){return 0.;}");
  }
}
