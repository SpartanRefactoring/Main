package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
@SuppressWarnings({ "javadoc", "static-method" })//
public class SpecificityTest {
  private static final Specificity SPECIFICITY = new Specificity();

  @Test public void characterGreaterThanNull() {
    that(SPECIFICITY.compare(e("'a'"), e("null")), greaterThan(0));
  }
  @Test public void characterLessThanThis() {
    that(SPECIFICITY.compare(e("'1'"), e("this")), lessThan(0));
  }
  @Test public void defined() {
    that(Specificity.defined(e("12")), is(true));
    that(Specificity.defined(e("a+b")), is(false));
  }
  @Test public void generalGreaterThanClassConstant() {
    that(SPECIFICITY.compare(e("a+b"), e("AB_C")), greaterThan(0));
  }
  @Test public void generalGreaterThanInteger() {
    that(SPECIFICITY.compare(e("a+b"), e("12")), greaterThan(0));
  }
  @Test public void generalGreaterThanMutlipleParenthesizedNegativeInteger() {
    that(SPECIFICITY.compare(e("a+b"), e("(-(12))")), greaterThan(0));
  }
  @Test public void generalGreaterThanNegativeInteger() {
    that(SPECIFICITY.compare(e("a+b"), e("-12")), greaterThan(0));
  }
  @Test public void generalGreaterThanNull() {
    that(SPECIFICITY.compare(e("a+b"), e("null")), greaterThan(0));
  }
  @Test public void generalGreaterThanParenthesizedClassConstant() {
    that(SPECIFICITY.compare(e("a+b"), e("(AB_C)")), greaterThan(0));
  }
  @Test public void generalGreaterThanParenthesizedNegativeInteger() {
    that(SPECIFICITY.compare(e("a+b"), e("(-12)")), greaterThan(0));
  }
  @Test public void generalGreaterThanSingleLetterClassConstant() {
    that(SPECIFICITY.compare(e("a+b"), e("A")), greaterThan(0));
  }
  @Test public void generalGreaterThanSingleLetterClassConstantWithUnderscore() {
    that(SPECIFICITY.compare(e("a+b"), e("A_")), greaterThan(0));
  }
  @Test public void generalGreaterThanThis() {
    that(SPECIFICITY.compare(e("a+b"), e("this")), greaterThan(0));
  }
  @Test public void hexadecimalConstant() {
    that(Specificity.defined(e("0xff")), is(true));
    that(Specificity.defined(e("0x7f")), is(true));
  }
  @Test public void hexadecimalConstantIsInteger() {
    that(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("12"))));
  }
  @Test public void hexadecimalConstantIsSame() {
    that(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("0x7f"))));
  }
  @Test public void integerConstantGreaterThanBooleanConstant() {
    that(SPECIFICITY.compare(e("12"), e("true")), greaterThan(0));
  }
  @Test public void integerGreaterThanNull() {
    that(SPECIFICITY.compare(e("12"), e("null")), greaterThan(0));
  }
  @Test public void integerLessThanThis() {
    that(SPECIFICITY.compare(e("12"), e("this")), lessThan(0));
  }
  @Test public void nullLessThanThis() {
    that(SPECIFICITY.compare(e("null"), e("this")), lessThan(0));
  }
  @Test public void pseudoConstantGreaterThanInteger() {
    that(SPECIFICITY.compare(e("AB_C"), e("(-(12))")), greaterThan(0));
  }
  @Test public void stringGreaterThanNull() {
    that(SPECIFICITY.compare(e("\"12\""), e("null")), greaterThan(0));
  }
  @Test public void stringLessThanThis() {
    that(SPECIFICITY.compare(e("\"12\""), e("this")), lessThan(0));
  }
  @Test public void thisGreaterThanNull() {
    that(SPECIFICITY.compare(e("this"), e("null")), greaterThan(0));
  }
  @Test public void twoDistinctClassConstants() {
    that(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("SPONGE"))));
  }
  @Test public void twoDistinctClassConstantsWithDigits() {
    that(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("B2B"))));
  }
  @Test public void twoIdenticalClassConstants() {
    that(Specificity.Level.of(e("SPONGE")), is(Specificity.Level.of(e("SPONGE"))));
  }
  @Test public void undefinedLevel() {
    that(Specificity.Level.of(e("a+b")), is(Specificity.Level.values().length));
  }
}
