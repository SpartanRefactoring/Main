package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test case for {@link ForWithEndingBreakToDoWhile}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-04 */
@SuppressWarnings("static-method")
public class Issue1220 {
  @Test public void a() {
    trimmingOf("for (int a = 0; a < 10; ++a) { b = b * c; c = c + 5; if (!d[a].e().f(d[0].e())) break; }") //
        .using(new ForWithEndingBreakToDoWhile(), ForStatement.class) //
        .stays()//
    ;
  }
}
