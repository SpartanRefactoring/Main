package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Test Class for the Bug described on Issue #1013
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2017-01-16 */
@SuppressWarnings("static-method")
public class Issue1013 {
  @Test public void t1() {
    topDownTrimming("a[x] = a[x] + x++;++x;").stays();
  }

  @Test public void t2() {
    topDownTrimming("a[x] = x;++x;").stays();
  }
}
