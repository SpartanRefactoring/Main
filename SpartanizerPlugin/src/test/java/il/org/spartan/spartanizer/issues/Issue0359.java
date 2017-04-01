package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered 
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0359 {
  @Test public void b() {
    trimmingOf("int i;++i;")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("int i=f();")//
        .gives("f();")//
        .stays();
  }

  @Test public void t20() {
    trimmingOf("for(;b==q++;){int i;}")//
        .gives("while(b==q++){int i;}")//
        .gives("while(b==q++){}")//
        .gives("while(b==q++);")//
        .stays();
  }
}
