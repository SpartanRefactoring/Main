package org.spartan.refactoring.utils;

import static org.spartan.hamcrest.CoreMatchers.is;
import static org.spartan.hamcrest.MatcherAssert.assertThat;
import static org.spartan.hamcrest.OrderingComparison.greaterThan;
import static org.spartan.hamcrest.OrderingComparison.lessThan;
import static org.spartan.refactoring.utils.Into.e;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class SpecificityTest {
  private static final Specificity SPECIFICITY = new Specificity();
  @Test public void characterGreaterThanNull() {
    assertThat(SPECIFICITY.compare(e("'a'"), e("null")), greaterThan(0));
  }
  @Test public void characterLessThanThis() {
    assertThat(SPECIFICITY.compare(e("'1'"), e("this")), lessThan(0));
  }
  @Test public void defined() {
    assertThat(Specificity.defined(e("12")), is(true));
    assertThat(Specificity.defined(e("a+b")), is(false));
  }
  @Test public void generalGreaterThanClassConstant() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("AB_C")), greaterThan(0));
  }
  @Test public void generalGreaterThanInteger() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("12")), greaterThan(0));
  }
  @Test public void generalGreaterThanMutlipleParenthesizedNegativeInteger() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("(-(12))")), greaterThan(0));
  }
  @Test public void generalGreaterThanNegativeInteger() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("-12")), greaterThan(0));
  }
  @Test public void generalGreaterThanNull() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("null")), greaterThan(0));
  }
  @Test public void generalGreaterThanParenthesizedClassConstant() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("(AB_C)")), greaterThan(0));
  }
  @Test public void generalGreaterThanParenthesizedNegativeInteger() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("(-12)")), greaterThan(0));
  }
  @Test public void generalGreaterThanSingleLetterClassConstant() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("A")), greaterThan(0));
  }
  @Test public void generalGreaterThanSingleLetterClassConstantWithUnderscore() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("A_")), greaterThan(0));
  }
  @Test public void generalGreaterThanThis() {
    assertThat(SPECIFICITY.compare(e("a+b"), e("this")), greaterThan(0));
  }
  @Test public void hexadecimalConstant() {
    assertThat(Specificity.defined(e("0xff")), is(true));
    assertThat(Specificity.defined(e("0x7f")), is(true));
  }
  @Test public void hexadecimalConstantIsInteger() {
    assertThat(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("12"))));
  }
  @Test public void hexadecimalConstantIsSame() {
    assertThat(Specificity.Level.of(e("0xff")), is(Specificity.Level.of(e("0x7f"))));
  }
  @Test public void integerConstantGreaterThanBooleanConstant() {
    assertThat(SPECIFICITY.compare(e("12"), e("true")), greaterThan(0));
  }
  @Test public void integerGreaterThanNull() {
    assertThat(SPECIFICITY.compare(e("12"), e("null")), greaterThan(0));
  }
  @Test public void integerLessThanThis() {
    assertThat(SPECIFICITY.compare(e("12"), e("this")), lessThan(0));
  }
  @Test public void nullLessThanThis() {
    assertThat(SPECIFICITY.compare(e("null"), e("this")), lessThan(0));
  }
  @Test public void pseudoConstantGreaterThanInteger() {
    assertThat(SPECIFICITY.compare(e("AB_C"), e("(-(12))")), greaterThan(0));
  }
  @Test public void stringGreaterThanNull() {
    assertThat(SPECIFICITY.compare(e("\"12\""), e("null")), greaterThan(0));
  }
  @Test public void stringLessThanThis() {
    assertThat(SPECIFICITY.compare(e("\"12\""), e("this")), lessThan(0));
  }
  @Test public void thisGreaterThanNull() {
    assertThat(SPECIFICITY.compare(e("this"), e("null")), greaterThan(0));
  }
  @Test public void twoDistinctClassConstants() {
    assertThat(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("SPONGE"))));
  }
  @Test public void twoDistinctClassConstantsWithDigits() {
    assertThat(Specificity.Level.of(e("BOB")), is(Specificity.Level.of(e("B2B"))));
  }
  @Test public void twoIdenticalClassConstants() {
    assertThat(Specificity.Level.of(e("SPONGE")), is(Specificity.Level.of(e("SPONGE"))));
  }
  @Test public void undefinedLevel() {
    assertThat(Specificity.Level.of(e("a+b")), is(Specificity.Level.values().length));
  }
}
