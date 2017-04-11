package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for Issue 1055
 * @author Yossi Gil
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1055 {
  @Test public void vanilla() {
    topDownTrimming("switch(x) { case 1: { y=2; } } ") //
        .gives("switch(x) { case 1: y = 2; }") //
    ;
  }
}
