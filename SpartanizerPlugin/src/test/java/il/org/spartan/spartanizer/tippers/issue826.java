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
        "    int i = 5*6*7;" + //
        "    if (++i < 5)" + //
        "      a(i);" + //
        "    else" + //
        "      a(i);"//
    ).gives("// Edit this to reflect your expectation, but leave this comment" + //
        "" + //
        "public void b() {" + //
        "    int i = 210;" + //
        "    if (++i < 5)" + //
        "      a(i);" + //
        "    else" + //
        "      a(i);"//
    );
  }
}
