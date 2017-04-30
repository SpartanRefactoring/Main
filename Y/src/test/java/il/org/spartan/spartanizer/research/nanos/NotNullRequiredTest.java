package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link NotNullRequired}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class NotNullRequiredTest {
  @Test public void a() {
    trimmingOf("void m(){if(x == null) return; use(); use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .gives("void m(){assert x!=null;use();use();}") //
        .stays();
  }

  @Test public void a2() {
    trimmingOf("void m(){if(x == null || y == null) return; use(); use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .gives("void m(){assert y!=null:x;use();use();}") //
        .stays();
  }

  @Test public void b() {
    trimmingOf("void m(){if(x == null) return null; use(); use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .gives("void m(){assert x != null;use();use();}")//
        .stays();
  }

  @Test public void b2() {
    trimmingOf("void m(){if(x == null  || y == null) return null; use(); use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .gives("void m(){assert y!=null:x;use();use();}") //
        .stays();
  }

  @Test public void c() {
    trimmingOf("void m(){s(); if(x == null) return null; use(); use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void m(){if(x == null || null == abc.b) return null; use(); use();}")//
        .gives("void m(){if(x==null||abc.b==null)return null;use();use();}")//
        .using(new NotNullRequired(), IfStatement.class)//
        .gives("void m(){azzert.notNull(x,abc.b);use();use();}")//
        .gives("void m(){assert abc.b!=null:x;use();use();}") //
        .stays();
  }
}
