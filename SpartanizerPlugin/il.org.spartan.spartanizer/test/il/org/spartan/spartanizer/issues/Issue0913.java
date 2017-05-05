package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for a bug in {@link RemoveRedundantSwitchCases} Related
 * to {@link Issue880}
 * @author Yuval Simon
 * @since 2016-12-09 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0913 {
  @Test public void a() {
    trimmingOf("switch(GuessedContex.find(p)) {case VLOCK_LOOK_ALIKE:return into.cu(p);case EXPRESSION_LOOK_ALIKE:return into.e(p);default:break;}")//
        .gives("switch(GuessedContex.find(p)){case EXPRESSION_LOOK_ALIKE:return into.e(p);case VLOCK_LOOK_ALIKE:return into.cu(p);default:break;}")
        .stays();
  }
}
