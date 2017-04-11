package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link PreconditionNotNull}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class PreconditionNotNullTest {
  @Test public void a() {
    topDownTrimming("void m(){if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .gives("void m(){assert x!=null;use();use();}") //
        .stays();
  }

  @Test public void a2() {
    topDownTrimming("void m(){if(x == null || y == null) return; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .gives("void m(){assert y!=null:x;use();use();}") //
        .stays();
  }

  @Test public void b() {
    topDownTrimming("void m(){if(x == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x);use();use();}")//
        .gives("void m(){assert x != null;use();use();}")//
        .stays();
  }

  @Test public void b2() {
    topDownTrimming("void m(){if(x == null  || y == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,y);use();use();}")//
        .gives("void m(){assert y!=null:x;use();use();}") //
        .stays();
  }

  @Test public void c() {
    topDownTrimming("void m(){s(); if(x == null) return null; use(); use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("void m(){if(x == null || null == abc.b) return null; use(); use();}")//
        .gives("void m(){if(x==null||abc.b==null)return null;use();use();}")//
        .using(IfStatement.class, new PreconditionNotNull())//
        .gives("void m(){azzert.notNull(x,abc.b);use();use();}")//
        .gives("void m(){assert abc.b!=null:x;use();use();}") //
        .stays();
  }
}
