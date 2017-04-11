package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for the GitHub issue thus numbered. case of inlining into the
 * expression of an enhanced for
 * @author Yossi Gil
 * @since 2017-03-16 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0120 {
  @Test public void a1() {
    trimminKof("\"a\"+\"b\"")//
        .gives("\"ab\"");
  }

  @Test public void a2() {
    trimminKof("\"abc\"+\"de\"+\"fgh\"")//
        .gives("\"abcdefgh\"");
  }

  @Test public void a3() {
    trimminKof("\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("\"abc\"+a.toString()+\"defgh\"");
  }

  @Test public void a4() {
    trimminKof("c.toString()+\"abc\"+a.toString()+\"de\"+\"fgh\"")//
        .gives("c.toString()+\"abc\"+a.toString()+\"defgh\"");
  }
}
