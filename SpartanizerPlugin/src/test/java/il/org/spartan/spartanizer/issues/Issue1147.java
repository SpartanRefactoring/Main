package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test case for {@link SwitchBranchSort}
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-25 */
@SuppressWarnings("static-method")
public class Issue1147 {
  @Test public void t1() {
    trimmingOf("switch (digits(round3(¢))) {" + "case -1:" + "case 0:" + "return \"%.3f\";" + "case 1:" + "return \"%.2f\";" + "case 2:"
        + "return \"%.1f\";" + "default:" + "return \"%.0f\";" + "}").stays();
  }

  // TODO Yuval Simon - use "using(...)" to make sure your tipper is called even
  // if it is not in the toolbox
  @Test public void t2() {
    trimmingOf("switch (digits(round3(¢))) {" + "case 0:" + "case 1:" + "return \"%.3f\";" + "case -1:" + "return \"%.2f\";" + "case 2:"
        + "return \"%.1f\";" + "default:" + "return \"%.0f\";" + "}")
            .using(SwitchStatement.class,new SwitchBranchSort())
            .gives("switch (digits(round3(¢))) {" + "case -1:" + "return \"%.2f\";" + "case 0:" + "case 1:" + "return \"%.3f\";" + "case 2:"
                + "return \"%.1f\";" + "default:" + "return \"%.0f\";" + "}")
            .stays();
  }
}
