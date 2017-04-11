package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** testing {@code ForRedundantContinue}
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01 */
@SuppressWarnings("static-method")
public class Issue0882 {
  @Test public void forTestNoChange() {
    topDownTrimming("for(int ¢=0;¢<5;++¢)++¢;")//
        .stays();
  }

  @Test public void mainTest() {
    topDownTrimming("for(int ¢=0;¢<5;++¢) continue;").gives("for(int ¢=0;¢<5;++¢) ;").stays();
  }

  @Test public void mainTestFullBlock() {
    topDownTrimming("for(int ¢=0;¢<5;++¢) {++¢;continue;}")//
        .gives("for(int ¢=0;¢<5;++¢) {++¢;}")//
        .gives("for(int ¢=0;¢<5;++¢) ++¢;")//
        .stays();
  }
}
