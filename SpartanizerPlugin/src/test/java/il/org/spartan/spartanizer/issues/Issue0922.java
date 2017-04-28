package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link ExpressionStatementAssertTrueFalse}
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0922 {
  @Test public void a$01() {
    trimmingOf("assertTrue(true);")//
        .gives("assert true;")//
        .stays();
  }

  @Test public void a$02() {
    trimmingOf("assertFalse(true);")//
        .gives("assert false;")//
        .stays();
  }

  @Test public void a$03() {
    trimmingOf("assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$04() {
    trimmingOf("assertFalse(T);")//
        .gives("assert !T;")//
        .stays();
  }

  @Test public void a$05() {
    trimmingOf("assertTrue(M, true);")//
        .gives("assert true:M;")//
        .stays();
  }

  @Test public void a$06() {
    trimmingOf("assertFalse(M, true);")//
        .gives("assert false:M;")//
        .stays();
  }

  @Test public void a$07() {
    trimmingOf("assertTrue(M, T);")//
        .gives("assert T:M;")//
        .stays();
  }

  @Test public void a$08() {
    trimmingOf("assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$09() {
    trimmingOf("Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$10() {
    trimmingOf("org.junit.Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$11() {
    trimmingOf("org.junit.Assert.assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$12() {
    trimmingOf("Assert.assertNotNull(T);")//
        .gives("assert T != null;")//
        .stays();
  }

  @Test public void a$13() {
    trimmingOf("Assert.assertNotNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }

  @Test public void a$14() {
    trimmingOf("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void a$15() {
    trimmingOf("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("azzert.notNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }
}
