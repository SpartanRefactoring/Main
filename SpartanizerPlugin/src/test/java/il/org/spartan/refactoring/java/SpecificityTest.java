package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.java.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class SpecificityTest {
  private static final Specificity SPECIFICITY = new Specificity();

  @Test public void characterGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("'a'"), e("null")), greaterThan(0));
  }

  @Test public void characterLessThanThis() {
    azzert.that(SPECIFICITY.compare(e("'1'"), e("this")), lessThan(0));
  }

  @Test public void defined() {
    azzert.that(Specificity.defined(e("12")), is(true));
    azzert.that(Specificity.defined(e("a+b")), is(false));
  }

  @Test public void generalGreaterThanClassConstant() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("AB_C")), greaterThan(0));
  }

  @Test public void generalGreaterThanInteger() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("12")), greaterThan(0));
  }

  @Test public void generalGreaterThanMutlipleParenthesizedNegativeInteger() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("(-(12))")), greaterThan(0));
  }

  @Test public void generalGreaterThanNegativeInteger() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("-12")), greaterThan(0));
  }

  @Test public void generalGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("null")), greaterThan(0));
  }

  @Test public void generalGreaterThanParenthesizedClassConstant() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("(AB_C)")), greaterThan(0));
  }

  @Test public void generalGreaterThanParenthesizedNegativeInteger() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("(-12)")), greaterThan(0));
  }

  @Test public void generalGreaterThanSingleLetterClassConstant() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("A")), greaterThan(0));
  }

  @Test public void generalGreaterThanSingleLetterClassConstantWithUnderscore() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("A_")), greaterThan(0));
  }

  @Test public void generalGreaterThanThis() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("this")), greaterThan(0));
  }

  @Test public void hexadecimalConstant() {
    azzert.that(Specificity.defined(e("0xff")), is(true));
    azzert.that(Specificity.defined(e("0x7f")), is(true));
  }

  @Test public void hexadecimalConstantIsInteger() {
    azzert.that(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("12"))));
  }

  @Test public void hexadecimalConstantIsSame() {
    azzert.that(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("0x7f"))));
  }

  @Test public void integerConstantGreaterThanBooleanConstant() {
    azzert.that(SPECIFICITY.compare(e("12"), e("true")), greaterThan(0));
  }

  @Test public void integerGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("12"), e("null")), greaterThan(0));
  }

  @Test public void integerLessThanThis() {
    azzert.that(SPECIFICITY.compare(e("12"), e("this")), lessThan(0));
  }

  @Test public void nullLessThanThis() {
    azzert.that(SPECIFICITY.compare(e("null"), e("this")), lessThan(0));
  }

  @Test public void pseudoConstantGreaterThanInteger() {
    azzert.that(SPECIFICITY.compare(e("AB_C"), e("(-(12))")), greaterThan(0));
  }

  @Test public void stringGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("\"12\""), e("null")), greaterThan(0));
  }

  @Test public void stringLessThanThis() {
    azzert.that(SPECIFICITY.compare(e("\"12\""), e("this")), lessThan(0));
  }

  @Test public void thisGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("this"), e("null")), greaterThan(0));
  }

  @Test public void twoDistinctClassConstants() {
    azzert.that(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("SPONGE"))));
  }

  @Test public void twoDistinctClassConstantsWithDigits() {
    azzert.that(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("B2B"))));
  }

  @Test public void twoIdenticalClassConstants() {
    azzert.that(Specificity.Level.of(e("SPONGE")), is(Specificity.Level.of(e("SPONGE"))));
  }

  @Test public void undefinedLevel() {
    azzert.that(Specificity.Level.of(e("a+b")), is(Specificity.Level.values().length));
  }
}
