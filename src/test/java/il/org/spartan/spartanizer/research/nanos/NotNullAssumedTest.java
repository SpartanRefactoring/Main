package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link NotNullAssumed}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class NotNullAssumedTest {
  @Test public void a() {
    trimmingOf("statement(); if(x == null) return; use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .gives("statement();assert x!=null;use();use();") //
        .stays();
  }

  @Test public void sanity() {
    trimmingOf("statement(); azzert.notNull(x); use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .stays();
  }

  @Test public void a2() {
    trimmingOf("statement(); if(x == null || y == null) return; use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .gives("statement();assert y!=null:x;use();use();") //
        .stays();
  }

  @Test public void b() {
    trimmingOf("statement(); if(x == null) return null; use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .gives("statement();assert x!=null;use();use();") //
        .stays();
  }

  @Test public void b2() {
    trimmingOf("statement(); if(x == null || y == null) return null; use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .gives("statement();assert y!=null:x;use();use();") //
        .stays();
  }

  @Test public void respect() {
    trimmingOf("void m(){if(x == null) return; use(); use();}")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .stays();
  }

  @Test public void respect2() {
    trimmingOf("void m(){use(); if(x == null) return false; use(); use();}")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .stays();
  }

  @Test public void respect3() {
    trimmingOf("void m(){use(); if(x == null) return; use(); use();}")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .gives("void m(){use();azzert.notNull(x);use();use();}") //
        .gives("void m(){use();assert x!=null;use();use();}") //
        .stays();
  }

  @Test public void respect4() {
    trimmingOf("statement(); if(x == null) return false; use(); use();")//
        .using(new NotNullAssumed(), IfStatement.class)//
        .stays();
  }
}
