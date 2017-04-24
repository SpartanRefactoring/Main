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
    trimminKof("assertTrue(true);")//
        .gives("assert true;")//
        .stays();
  }

  @Test public void a$02() {
    trimminKof("assertFalse(true);")//
        .gives("assert false;")//
        .stays();
  }

  @Test public void a$03() {
    trimminKof("assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$04() {
    trimminKof("assertFalse(T);")//
        .gives("assert !T;")//
        .stays();
  }

  @Test public void a$05() {
    trimminKof("assertTrue(M, true);")//
        .gives("assert true:M;")//
        .stays();
  }

  @Test public void a$06() {
    trimminKof("assertFalse(M, true);")//
        .gives("assert false:M;")//
        .stays();
  }

  @Test public void a$07() {
    trimminKof("assertTrue(M, T);")//
        .gives("assert T:M;")//
        .stays();
  }

  @Test public void a$08() {
    trimminKof("assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$09() {
    trimminKof("Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$10() {
    trimminKof("org.junit.Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$11() {
    trimminKof("org.junit.Assert.assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$12() {
    trimminKof("Assert.assertNotNull(T);")//
        .gives("assert T != null;")//
        .stays();
  }

  @Test public void a$13() {
    trimminKof("Assert.assertNotNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }

  @Test public void a$14() {
    trimminKof("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void a$15() {
    trimminKof("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("azzert.notNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }
}
