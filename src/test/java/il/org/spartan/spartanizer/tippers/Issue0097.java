package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO:  Stav Namir
 please add a description 
 @author Stav Namir
 * @since 2016-08-29 
 */

@SuppressWarnings("static-method")
public final class Issue0097 {
  @Test public void test01() {
    trimmingOf(
        "\"Spartanizing '\" + javaProject.getElementName() + \"' project \\n\" + \n\"Completed in \" + (1 + i) + \" passes. \\n\" + \n\"Total changes: \" + (initialCount - finalCount) + \"\\n\" + \n\"Tips before: \" + initialCount + \"\\n\" + \n\"Tips after: \" + finalCount + \"\\n\" + \nmessage")
            .stays();
  }
}

