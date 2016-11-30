package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Tomer Dragucki
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class issue826 {
  @Test public void a() {
    trimmingOf("" + //
        "A() ? 8 : 8"//
    ).stays();
  }

  @Test public void b() {
    trimmingOf("" + //
        "public void b() {" + //
        "    int i = 210;" + //
        "    if (++i < 5)" + //
        "      a(i);" + //
        "    else" + //
        "      a(i);"//
    ).stays();
  }
}
