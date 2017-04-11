package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test of {@link AssignmentAndUpdateAssignmentToSame}
 * @author Yossi Gil
 * @since 2017-03-04 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1133 {
  @Test public void and() {
    topDownTrimming("a =2; a &= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 & 3;")//
    ;
  }

  @Test public void divides() {
    topDownTrimming("a =2; a /= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 / 3;")//
    ;
  }

  @Test public void minus() {
    topDownTrimming("a =2; a -= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 - 3;")//
    ;
  }

  @Test public void or() {
    topDownTrimming("a =2; a |= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 | 3;")//
    ;
  }

  @Test public void function() {
    topDownTrimming("a =b; a += f(c,d);")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = b + f(c,d);")//
    ;
  }

  @Test public void plus() {
    topDownTrimming("a =2; a += 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 + 3;")//
    ;
  }

  @Test public void times() {
    topDownTrimming("a =2; a *= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateAssignmentToSame()) //
        .gives("a = 2 * 3;")//
    ;
  }
}
