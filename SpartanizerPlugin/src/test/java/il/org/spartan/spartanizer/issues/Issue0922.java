package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link ExpressionStatementAssertTrueFalse}
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0922 {
  @Test public void a$01() {
    topDownTrimming("assertTrue(true);")//
        .gives("assert true;")//
        .stays();
  }

  @Test public void a$02() {
    topDownTrimming("assertFalse(true);")//
        .gives("assert false;")//
        .stays();
  }

  @Test public void a$03() {
    topDownTrimming("assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$04() {
    topDownTrimming("assertFalse(T);")//
        .gives("assert !T;")//
        .stays();
  }

  @Test public void a$05() {
    topDownTrimming("assertTrue(M, true);")//
        .gives("assert true:M;")//
        .stays();
  }

  @Test public void a$06() {
    topDownTrimming("assertFalse(M, true);")//
        .gives("assert false:M;")//
        .stays();
  }

  @Test public void a$07() {
    topDownTrimming("assertTrue(M, T);")//
        .gives("assert T:M;")//
        .stays();
  }

  @Test public void a$08() {
    topDownTrimming("assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$09() {
    topDownTrimming("Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$10() {
    topDownTrimming("org.junit.Assert.assertFalse(M, T);")//
        .gives("assert !T:M;")//
        .stays();
  }

  @Test public void a$11() {
    topDownTrimming("org.junit.Assert.assertTrue(T);")//
        .gives("assert T;")//
        .stays();
  }

  @Test public void a$12() {
    topDownTrimming("Assert.assertNotNull(T);")//
        .gives("assert T != null;")//
        .stays();
  }

  @Test public void a$13() {
    topDownTrimming("Assert.assertNotNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }

  @Test public void a$14() {
    topDownTrimming("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void a$15() {
    topDownTrimming("Assert.assertNull(message, T);")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("azzert.notNull(message, T);")//
        .gives("assert T != null: message;")//
        .stays();
  }
}
