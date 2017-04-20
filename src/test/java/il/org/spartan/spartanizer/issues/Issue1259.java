package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import java.util.logging.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case for bug in {@link AssignmentAndAssignmentOfSameValue}
 * @author Yuval Simon
 * @since 2017-04-19 */
@SuppressWarnings("static-method")
@Ignore("Dor Maayan") 
public class Issue1259 {
  @Test(timeout = 2500) public void test() {
    trimminKof("class Element extends AccessibleObject implements Member {" + //
        "private final AccessibleObject accessibleObject;" + //
        "private final Member member;" + //
        "<M extends AccessibleObject & Member> Element(M accessibleObject) {" + //
        "checkNotNull(accessibleObject);" + //
        "this.accessibleObject = accessibleObject;" + //
        "this.member = accessibleObject;" + //
        "}" + //
        "}").gives("class Element extends AccessibleObject implements Member {" + //
            "private final AccessibleObject accessibleObject;" + //
            "private final Member member;" + //
            "<M extends AccessibleObject & Member> Element(M member) {" + //
            "checkNotNull(member);" + //
            "this.accessibleObject = this.member = accessibleObject;" + //
            "}" + //
            "}");
  }

  @Test(timeout = 3500) public void t1() {
    TraversalMonitor.logger.setLevel(Level.ALL);
    trimminKof("class E extends B {" + //
        "A a;" + //
        "M m;" + //
        "E(M m) {" + //
        "checkNotNull(m);" + //
        "this.a = m;" + //
        "this.m = m;" + //
        "}" + //
        "}")//
            .using(new ConstructorRenameParameters())//
            .gives("class E extends B {" + //
                "A a;" + //
                "M m;" + //
                "E(M m) {" + //
                "checkNotNull(m);" + //
                "this.a = a; this.m = m;" + //
                "}" + //
                "}");
  }
}
