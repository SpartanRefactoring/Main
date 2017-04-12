package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests of {@link AssignmentAndAssignmentToSame}
 * @author Yossi Gil
 * @since 2017-03-31 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class issue0449 {
  @Test public void a() {
    trimminKof("s = s.replaceAll(b,c); s=s.replaceAll(d,e);")//
        .using(new AssignmentAndAssignmentToSame(), Assignment.class)//
        .gives("s = s.replaceAll(b,c).replaceAll(d,e);");
  }
}
