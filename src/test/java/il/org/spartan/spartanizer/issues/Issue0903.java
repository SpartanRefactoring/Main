package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link ReturnToBreakFiniteFor} of previously failed
 * tests. Related to {@link Issue0131}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0903 {
  @Test public void a20() {
    trimmingOf("while(i>9)if(i==5)return x;return x;")//
        .gives("while(i>9)if(i==5)break;return x;");
  }

  @Test public void a70() {
    trimmingOf("while(i>5)return x;return x;")//
        .gives("while(i>5)break;return x;")//
        .stays();
  }

  @Test public void a80() {
    trimmingOf("while(i>5)if(tipper=4)return x;return x;")//
        .gives("while(i>5)if(tipper=4)break;return x;");
  }
}
