package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0359 {
  @Test public void b() {
    topDownTrimming("int i;++i;")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("int i=f();")//
        .gives("f();")//
        .stays();
  }

  @Test public void t20() {
    topDownTrimming("for(;b==q++;){int i;}")//
        .gives("while(b==q++){int i;}")//
        .gives("while(b==q++){}")//
        .gives("while(b==q++);")//
        .stays();
  }
}
