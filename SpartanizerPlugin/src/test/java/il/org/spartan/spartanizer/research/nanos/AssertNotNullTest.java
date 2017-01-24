package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class AssertNotNullTest {
  @Test public void a() {
    trimmingOf("statement(); if(x == null) return; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .stays();
  }

  @Test public void a2() {
    trimmingOf("statement(); if(x == null || y == null) return; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("statement(); if(x == null) return null; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .stays();
  }

  @Test public void b2() {
    trimmingOf("statement(); if(x == null || y == null) return null; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("void m(){if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new AssertNotNull())//
        .stays();
  }
}
