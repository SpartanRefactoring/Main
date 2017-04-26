package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link AssignmentAndAssignmentToSame}
 * @author Yossi Gil
 * @since 2017-03-31 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class issue0449 {
  @Test public void a() {
    trimmingOf("s = s.replaceAll(b,c); s=s.replaceAll(d,e);")//
        .using(new AssignmentAndAssignmentToSame(), Assignment.class)//
        .gives("s = s.replaceAll(b,c).replaceAll(d,e);");
  }
}
