package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** See GitHub issue with thus numbered
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
@Ignore
public class Issue1125 {
  @Test public void a() {
    trimmingOf("for(x();y();z()){}")//
        .gives("for(x();y();z());")//
        .stays()//
    ;
  }
}
