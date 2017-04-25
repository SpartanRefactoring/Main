package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;
import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Testing testing mechanisms.
 * @author Ori Roth
 * @since 2017-04-13 */
@Ignore
public class Issue1249 {
  private final Tipper<ExpressionStatement> fail = new FailingTipper<>();

  @Test(expected = Exception.class) public void a() {
    trimmingOf("f();").using(fail, ExpressionStatement.class).doesNotCrash();
  }

  @Test(expected = Exception.class) public void b() {
    bloatingOf("f();").using(fail, ExpressionStatement.class).doesNotCrash();
  }
}
