package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class PreconditionNotNullTest {
  @Test public void a() {
    trimmingOf("void m(){if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .stays();
  }

  @Test public void a2() {
    trimmingOf("void m(){if(x == null || y == null) return; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("void m(){if(x == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .stays();
  }

  @Test public void b2() {
    trimmingOf("void m(){if(x == null  || y == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("void m(){s(); if(x == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void m(){if(x == null || null == abc.b) return null; use(); use();}")//
        .gives("void m(){if(x==null||abc.b==null)return null;use();use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,abc.b);use();use();}")//
        .stays();
  }
}
