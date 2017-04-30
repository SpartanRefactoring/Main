package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link LetInMethod}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
@SuppressWarnings("static-method")
public class LetInMethodTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new LetInMethod());
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

  @Test public void e() {
    assert is(//
        "private static SimpleName peelIdentifier(final Statement s, final String id) {"//
            + "    final List<SimpleName> $ = occurencesOf(s, id);"//
            + "    return $.size() != 1 ? null : first($);"//
            + " }");
  }

  @Test public void f() {
    assert not("boolean foo(){A x = foo(); return bar(y,y);}");
  }
}
