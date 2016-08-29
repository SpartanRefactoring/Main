package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.java.RationalType.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class KindTest {
  @Ignore public static class Pending {
    // class for Pending tests that don't currently pass
    // TODO: look into toStrihg()
    @Test public void test124() {
      azzert.that(rationalType(Into.e("a.toString()")), is(STRING));
    }
  }

  public static class Working {
    /* First batch of tests looks into specific inner cases of the under methods
     * Designed to cover most of the cases of the code */
    @Test public void test01() {
      azzert.that(rationalType(Into.e("+2"), INT), is(INT));
    }

    @Test public void test02() {
      azzert.that(rationalType(Into.e("~2"), ALPHANUMERIC), is(INTEGRAL));
    }

    @Ignore("creates NumberLiteral instead of PrefixExpression, need to figure out why") @Test public void test03() {
      azzert.that(rationalType(Into.e("++3"), DOUBLE), is(DOUBLE));
    }

    @Test public void test04() {
      azzert.that(rationalType(Into.e("!x"), NOTHING), is(BOOLEAN));
    }

    @Test public void test05() {
      azzert.that(rationalType(Into.e("~'x'"), CHAR), is(INT));
    }

    @Test public void test06() {
      azzert.that(rationalType(Into.e("x+y"), NOTHING, NOTHING), is(ALPHANUMERIC));
    }

    @Test public void test07() {
      azzert.that(rationalType(Into.e("x+y"), INT, DOUBLE), is(DOUBLE));
    }

    @Test public void test08() {
      azzert.that(rationalType(Into.e("x+y"), INT, INT), is(INT));
    }

    @Test public void test09() {
      azzert.that(rationalType(Into.e("x+y"), STRING, STRING), is(STRING));
    }

    @Test public void test10() {
      azzert.that(rationalType(Into.e("x+y"), STRING, NULL), is(STRING));
    }

    @Test public void test11() {
      azzert.that(rationalType(Into.e("x+y"), NUMERIC, NULL), is(STRING));
    }

    @Test public void test12() {
      azzert.that(rationalType(Into.e("x+y"), LONG, INT), is(LONG));
    }

    @Test public void test13() {
      azzert.that(rationalType(Into.e("x+y"), LONG, INTEGRAL), is(LONG));
    }

    @Test public void test14() {
      azzert.that(rationalType(Into.e("x+y"), LONG, NUMERIC), is(NUMERIC));
    }

    @Test public void test15() {
      azzert.that(rationalType(Into.e("x+y"), INT, INTEGRAL), is(INTEGRAL));
    }

    @Test public void test16() {
      azzert.that(rationalType(Into.e("x&y"), INT, INT), is(INT));
    }

    @Test public void test17() {
      azzert.that(rationalType(Into.e("x|y"), INT, LONG), is(LONG));
    }

    @Test public void test18() {
      azzert.that(rationalType(Into.e("x<<y"), INTEGRAL, LONG), is(INTEGRAL));
    }

    @Test public void test19() {
      azzert.that(rationalType(Into.e("x%y"), NUMERIC, NOTHING), is(INTEGRAL));
    }

    @Test public void test20() {
      azzert.that(rationalType(Into.e("x>>y"), LONG, INTEGRAL), is(LONG));
    }

    @Test public void test21() {
      azzert.that(rationalType(Into.e("x^y"), NONNULL, INTEGRAL), is(INTEGRAL));
    }

    @Test public void test22() {
      azzert.that(rationalType(Into.e("x>y"), INT, INTEGRAL), is(BOOLEAN));
    }

    @Test public void test23() {
      azzert.that(rationalType(Into.e("x==y"), NONNULL, INTEGRAL), is(BOOLEAN));
    }

    @Test public void test24() {
      azzert.that(rationalType(Into.e("x!=y"), NUMERIC, BAPTIZED), is(BOOLEAN));
    }

    @Test public void test25() {
      azzert.that(rationalType(Into.e("x&&y"), BOOLEAN, BOOLEAN), is(BOOLEAN));
    }

    @Test public void test26() {
      azzert.that(rationalType(Into.e("x*y"), DOUBLE, NUMERIC), is(DOUBLE));
    }

    @Test public void test27() {
      azzert.that(rationalType(Into.e("x/y"), DOUBLE, INTEGRAL), is(DOUBLE));
    }

    @Test public void test28() {
      azzert.that(rationalType(Into.e("x-y"), INTEGRAL, LONG), is(LONG));
    }

    @Test public void test29() {
      azzert.that(rationalType(Into.e("x+y"), CHAR, CHAR), is(INT));
    }

    @Test public void test30() {
      azzert.that(rationalType(Into.e("x-y"), CHAR, INT), is(INT));
    }

    /* Second batch of tests uses kind with various, complex expression, as
     * users are expected to use it */
    @Test public void test101() {
      azzert.that(rationalType(Into.e("2 + (2.0)*1L")), is(DOUBLE));
    }

    @Test public void test102() {
      azzert.that(rationalType(Into.e("(int)(2 + (2.0)*1L)")), is(INT));
    }

    @Test public void test103() {
      azzert.that(rationalType(Into.e("(int)(2 + (2.0)*1L)==9.0")), is(BOOLEAN));
    }

    @Test public void test104() {
      azzert.that(rationalType(Into.e("9*3.0+f()")), is(DOUBLE));
    }

    @Test public void test105() {
      azzert.that(rationalType(Into.e("g()+f()")), is(ALPHANUMERIC));
    }

    @Test public void test106() {
      azzert.that(rationalType(Into.e("f(g()+h(),f(2))")), is(NOTHING));
    }

    @Test public void test107() {
      azzert.that(rationalType(Into.e("(((null)))")), is(NULL));
    }

    @Test public void test108() {
      azzert.that(rationalType(Into.e("f()+null")), is(STRING));
    }

    @Test public void test109() {
      azzert.that(rationalType(Into.e("(List)f()")), is(BAPTIZED));
    }

    @Test public void test110() {
      azzert.that(rationalType(Into.e("new List<Integer>()")), is(NONNULL));
    }

    @Test public void test111() {
      azzert.that(rationalType(Into.e("new Object()")), is(NONNULL));
    }

    @Test public void test112() {
      azzert.that(rationalType(Into.e("new String(\"hello\")")), is(STRING));
    }

    @Test public void test113() {
      azzert.that(rationalType(Into.e("\"a string\"")), is(STRING));
    }

    @Test public void test114() {
      azzert.that(rationalType(Into.e("'a'")), is(CHAR));
    }

    @Test public void test115() {
      azzert.that(rationalType(Into.e("5")), is(INT));
    }

    @Test public void test116() {
      azzert.that(rationalType(Into.e("5L")), is(LONG));
    }

    @Test public void test117() {
      azzert.that(rationalType(Into.e("5.0")), is(DOUBLE));
    }

    @Test public void test118() {
      azzert.that(rationalType(Into.e("true")), is(BOOLEAN));
    }

    @Test public void test119() {
      azzert.that(rationalType(Into.e("2+f()")), is(NUMERIC));
    }

    @Test public void test120() {
      azzert.that(rationalType(Into.e("2%f()")), is(INT));
    }

    @Test public void test121() {
      azzert.that(rationalType(Into.e("2<<f()")), is(INT));
    }

    @Test public void test122() {
      azzert.that(rationalType(Into.e("f()<<2")), is(INTEGRAL));
    }

    @Test public void test123() {
      azzert.that(rationalType(Into.e("f()||g()")), is(BOOLEAN));
    }

    @Test public void test124a() {
      azzert.that(rationalType(Into.e("(char)x")), is(CHAR));
    }

    @Test public void test124b() {
      azzert.that(rationalType(Into.e("(Character)x")), is(CHAR));
    }

    @Test public void test125a() {
      azzert.that(rationalType(Into.e("(int)x")), is(INT));
    }

    @Test public void test125b() {
      azzert.that(rationalType(Into.e("(Integer)x")), is(INT));
    }

    @Test public void test126a() {
      azzert.that(rationalType(Into.e("(long)x")), is(LONG));
    }

    @Test public void test126b() {
      azzert.that(rationalType(Into.e("(Long)x")), is(LONG));
    }

    @Test public void test127a() {
      azzert.that(rationalType(Into.e("(double)x")), is(DOUBLE));
    }

    @Test public void test127b() {
      azzert.that(rationalType(Into.e("(Double)x")), is(DOUBLE));
    }

    @Test public void test128a() {
      azzert.that(rationalType(Into.e("(boolean)x")), is(BOOLEAN));
    }

    @Test public void test128b() {
      azzert.that(rationalType(Into.e("(Boolean)x")), is(BOOLEAN));
    }

    @Test public void test129() {
      azzert.that(rationalType(Into.e("(String)x")), is(STRING));
    }

    @Test public void test130() {
      azzert.that(rationalType(Into.e("x++")), is(NUMERIC));
    }

    @Test public void test131() {
      azzert.that(rationalType(Into.e("7++")), is(INT));
    }

    @Ignore("creates CharacterLiteral instead of PrefixExpression, need to figure out why") @Test public void test132() {
      azzert.that(rationalType(Into.e("'a'--")), is(INT));
    }

    @Test public void test133() {
      azzert.that(rationalType(Into.e("2L++")), is(LONG));
    }

    @Test public void test134() {
      azzert.that(rationalType(Into.e("(-3.0)--")), is(DOUBLE));
    }
  }
}
