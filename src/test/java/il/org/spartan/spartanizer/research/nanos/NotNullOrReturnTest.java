package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link NotNullAssumed}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class NotNullOrReturnTest {
  @Test public void a() {
    trimminKof("statement(); if(x == null) return; use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .gives("statement();assert x!=null;use();use();") //
        .stays();
  }

  @Test public void sanity() {
    trimminKof("statement(); azzert.notNull(x); use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .stays();
  }

  @Test public void a2() {
    trimminKof("statement(); if(x == null || y == null) return; use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .gives("statement();assert y!=null:x;use();use();") //
        .stays();
  }

  @Test public void b() {
    trimminKof("statement(); if(x == null) return null; use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .gives("statement();assert x!=null;use();use();") //
        .stays();
  }

  @Test public void b2() {
    trimminKof("statement(); if(x == null || y == null) return null; use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .gives("statement(); azzert.notNull(x,y); use(); use();")//
        .gives("statement();assert y!=null:x;use();use();") //
        .stays();
  }

  @Test public void respect() {
    trimminKof("void m(){if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new NotNullAssumed())//
        .stays();
  }

  @Test public void respect2() {
    trimminKof("void m(){use(); if(x == null) return false; use(); use();}")//
        .using(IfStatement.class, new NotNullAssumed())//
        .stays();
  }

  @Test public void respect3() {
    trimminKof("void m(){use(); if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new NotNullAssumed())//
        .gives("void m(){use();azzert.notNull(x);use();use();}") //
        .gives("void m(){use();assert x!=null;use();use();}") //
        .stays();
  }

  @Test public void respect4() {
    trimminKof("statement(); if(x == null) return false; use(); use();")//
        .using(IfStatement.class, new NotNullAssumed())//
        .stays();
  }
}
