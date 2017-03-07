package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Test of {@link AssignmentAndUpdateToSame}
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 *         <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-04 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1133 {
  @Test public void and() {
    trimmingOf("a =2; a &= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 & 3;")//
    ;
  }

  @Test public void divides() {
    trimmingOf("a =2; a /= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 / 3;")//
    ;
  }

  @Test public void minus() {
    trimmingOf("a =2; a -= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 - 3;")//
    ;
  }

  @Test public void or() {
    trimmingOf("a =2; a |= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 | 3;")//
    ;
  }

  @Test public void plus() {
    trimmingOf("a =2; a += 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 + 3;")//
    ;
  }

  @Test public void times() {
    trimmingOf("a =2; a *= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a = 2 * 3;")//
    ;
  }

  @Ignore @Test public void xor() {
    trimmingOf("a ^= 2; a ^= 3;")//
        .using(Assignment.class, new AssignmentAndUpdateToSame()) //
        .gives("a ^= 2 ^ 3;")//
    ;
  }
}
