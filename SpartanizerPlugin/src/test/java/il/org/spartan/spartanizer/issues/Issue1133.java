package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Test of {@link AssignmentAndUpdateAssignmentToSame}
 * @author Yossi Gil
 * @since 2017-03-04 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1133 {
  @Test public void and() {
    trimminKof("a =2; a &= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 & 3;")//
    ;
  }

  @Test public void divides() {
    trimminKof("a =2; a /= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 / 3;")//
    ;
  }

  @Test public void minus() {
    trimminKof("a =2; a -= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 - 3;")//
    ;
  }

  @Test public void or() {
    trimminKof("a =2; a |= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 | 3;")//
    ;
  }

  @Test public void function() {
    trimminKof("a =b; a += f(c,d);")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = b + f(c,d);")//
    ;
  }

  @Test public void plus() {
    trimminKof("a =2; a += 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 + 3;")//
    ;
  }

  @Test public void times() {
    trimminKof("a =2; a *= 3;")//
        .using(new AssignmentAndUpdateAssignmentToSame(), Assignment.class) //
        .gives("a = 2 * 3;")//
    ;
  }
}
