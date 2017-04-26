package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil
 * @since 2017-03-16 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0120 {
  @Test public void a1() {
    trimmingOf("\"a\"+\"b\"")//
        .gives("\"ab\"");
  }

  @Test public void a2() {
    trimmingOf("\"abc\"+\"de\"+\"fgh\"")//
        .gives("\"abcdefgh\"");
  }

  @Test public void a3() {
    trimmingOf("\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void a4() {
    trimmingOf("c.toString()+\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("c.toString()+\"abc\"+a.toString()+\"defgh\"");
  }
}
