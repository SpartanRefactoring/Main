package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test case for bug in {@link AssignmentAndAssignmentOfSameValue}
 * @author Yuval Simon
 * @since 2017-04-19 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1259 {
  @Test public void test() {
    trimminKof("class Element extends AccessibleObject implements Member {"
        + "private final AccessibleObject accessibleObject;"
        + "private final Member member;"
        + "<M extends AccessibleObject & Member> Element(M accessibleObject) {"
        + "checkNotNull(accessibleObject);"
        + "this.accessibleObject = accessibleObject;"
        + "this.member = accessibleObject;"
        + "}"
        + "}")
    .gives("class Element extends AccessibleObject implements Member {"
        + "private final AccessibleObject accessibleObject;"
        + "private final Member member;"
        + "<M extends AccessibleObject & Member> Element(M member) {"
        + "checkNotNull(member);"
        + "this.accessibleObject = this.member = accessibleObject;"
        + "}"
        + "}");
  }
}
