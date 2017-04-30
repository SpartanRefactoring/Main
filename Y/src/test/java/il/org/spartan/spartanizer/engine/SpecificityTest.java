/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Sep 8, 2016 */
package il.org.spartan.spartanizer.engine;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;

@SuppressWarnings({ "javadoc", "static-method" })
public final class SpecificityTest {
  private static final Comparator<Expression> SPECIFICITY = new specificity();

  @Test public void characterGreaterThanNull() {
    azzert.that(SPECIFICITY.compare(e("'a'"), e("null")), greaterThan(0));
  }

  @Test public void characterLessThanThis() {
    azzert.that(SPECIFICITY.compare(e("'1'"), e("this")), lessThan(0));
  }

  @Test public void defined() {
    assert specificity.defined(e("12"));
    assert !specificity.defined(e("a+b"));
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
    azzert.that(SPECIFICITY.compare(e("a+b"), e("A__")), greaterThan(0));
  }

  @Test public void generalGreaterThanThis() {
    azzert.that(SPECIFICITY.compare(e("a+b"), e("this")), greaterThan(0));
  }

  @Test public void hexadecimalConstant() {
    assert specificity.defined(e("0xff"));
    assert specificity.defined(e("0x7f"));
  }

  @Test public void hexadecimalConstantIsInteger() {
    azzert.that(specificity.Level.of(e("0xff")), is(specificity.Level.of(e("12"))));
  }

  @Test public void hexadecimalConstantIsSame() {
    azzert.that(specificity.Level.of(e("0xff")), is(specificity.Level.of(e("0x7f"))));
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
    azzert.that(specificity.Level.of(e("BOB")), is(specificity.Level.of(e("SPONGE"))));
  }

  @Test public void twoDistinctClassConstantsWithDigits() {
    azzert.that(specificity.Level.of(e("BOB")), is(specificity.Level.of(e("B2B"))));
  }

  @Test public void twoIdenticalClassConstants() {
    azzert.that(specificity.Level.of(e("SPONGE")), is(specificity.Level.of(e("SPONGE"))));
  }

  @Test public void undefinedLevel() {
    azzert.that(specificity.Level.of(e("a+b")), is(specificity.Level.values().length));
  }
}
