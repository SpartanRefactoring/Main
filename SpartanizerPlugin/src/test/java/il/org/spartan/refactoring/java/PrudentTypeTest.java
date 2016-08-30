package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.java.PrudentType.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class PrudentTypeTest {
  @Ignore public static class Pending {
    // class for Pending tests that don't currently pass
  }

  public static class Working {
    /* First batch of tests looks into specific inner cases of the under methods
     * Designed to cover most of the cases of the code */
    @Test public void test01() {
      azzert.that(prudent(Into.e("+2"), INT), is(INT));
    }

    @Test public void test02() {
      azzert.that(prudent(Into.e("~2"), ALPHANUMERIC), is(INTEGRAL));
    }

    @Ignore("creates NumberLiteral instead of PrefixExpression, need to figure out why") @Test public void test03() {
      azzert.that(prudent(Into.e("++3"), DOUBLE), is(DOUBLE));
    }

    @Test public void test04() {
      azzert.that(prudent(Into.e("!x"), NOTHING), is(BOOLEAN));
    }

    @Test public void test05() {
      azzert.that(prudent(Into.e("~'x'"), CHAR), is(INT));
    }

    @Test public void test06() {
      azzert.that(prudent(Into.e("x+y"), NOTHING, NOTHING), is(ALPHANUMERIC));
    }

    @Test public void test07() {
      azzert.that(prudent(Into.e("x+y"), INT, DOUBLE), is(DOUBLE));
    }

    @Test public void test08() {
      azzert.that(prudent(Into.e("x+y"), INT, INT), is(INT));
    }

    @Test public void test09() {
      azzert.that(prudent(Into.e("x+y"), STRING, STRING), is(STRING));
    }

    @Test public void test10() {
      azzert.that(prudent(Into.e("x+y"), STRING, NULL), is(STRING));
    }

    @Test public void test11() {
      azzert.that(prudent(Into.e("x+y"), NUMERIC, NULL), is(STRING));
    }

    @Test public void test12() {
      azzert.that(prudent(Into.e("x+y"), LONG, INT), is(LONG));
    }

    @Test public void test13() {
      azzert.that(prudent(Into.e("x+y"), LONG, INTEGRAL), is(LONG));
    }

    @Test public void test14() {
      azzert.that(prudent(Into.e("x+y"), LONG, NUMERIC), is(NUMERIC));
    }

    @Test public void test15() {
      azzert.that(prudent(Into.e("x+y"), INT, INTEGRAL), is(INTEGRAL));
    }

    @Test public void test16() {
      azzert.that(prudent(Into.e("x&y"), INT, INT), is(INT));
    }

    @Test public void test17() {
      azzert.that(prudent(Into.e("x|y"), INT, LONG), is(LONG));
    }

    @Test public void test18() {
      azzert.that(prudent(Into.e("x<<y"), INTEGRAL, LONG), is(INTEGRAL));
    }

    @Test public void test19() {
      azzert.that(prudent(Into.e("x%y"), NUMERIC, NOTHING), is(INTEGRAL));
    }

    @Test public void test20() {
      azzert.that(prudent(Into.e("x>>y"), LONG, INTEGRAL), is(LONG));
    }

    @Test public void test21() {
      azzert.that(prudent(Into.e("x^y"), NONNULL, INTEGRAL), is(INTEGRAL));
    }

    @Test public void test22() {
      azzert.that(prudent(Into.e("x>y"), INT, INTEGRAL), is(BOOLEAN));
    }

    @Test public void test23() {
      azzert.that(prudent(Into.e("x==y"), NONNULL, INTEGRAL), is(BOOLEAN));
    }

    @Test public void test24() {
      azzert.that(prudent(Into.e("x!=y"), NUMERIC, BAPTIZED), is(BOOLEAN));
    }

    @Test public void test25() {
      azzert.that(prudent(Into.e("x&&y"), BOOLEAN, BOOLEAN), is(BOOLEAN));
    }

    @Test public void test26() {
      azzert.that(prudent(Into.e("x*y"), DOUBLE, NUMERIC), is(DOUBLE));
    }

    @Test public void test27() {
      azzert.that(prudent(Into.e("x/y"), DOUBLE, INTEGRAL), is(DOUBLE));
    }

    @Test public void test28() {
      azzert.that(prudent(Into.e("x-y"), INTEGRAL, LONG), is(LONG));
    }

    @Test public void test29() {
      azzert.that(prudent(Into.e("x+y"), CHAR, CHAR), is(INT));
    }

    @Test public void test30() {
      azzert.that(prudent(Into.e("x-y"), CHAR, INT), is(INT));
    }
    
    //tests for NumeberLiterals
    
    @Test public void test31() {
      azzert.that(prudent(Into.e("3")), is(INT));
    }
    
    @Test public void test32() {
      azzert.that(prudent(Into.e("3l")), is(LONG));
    }
    
    @Test public void test33() {
      azzert.that(prudent(Into.e("3L")), is(LONG));
    }
    
    @Test public void test34() {
      azzert.that(prudent(Into.e("3d")), is(DOUBLE));
    }
    
    @Test public void test35() {
      azzert.that(prudent(Into.e("3D")), is(DOUBLE));
    }
    
    @Test public void test36() {
      azzert.that(prudent(Into.e("3.0d")), is(DOUBLE));
    }
    
    @Test public void test37() {
      azzert.that(prudent(Into.e("3.02D")), is(DOUBLE));
    }
    
    @Test public void test38() {
      azzert.that(prudent(Into.e("3.098")), is(DOUBLE));
    }


    /* Second batch of tests uses kind with various, complex expression, as
     * users are expected to use it */
    @Test public void test101() {
      azzert.that(prudent(Into.e("2 + (2.0)*1L")), is(DOUBLE));
    }

    @Test public void test102() {
      azzert.that(prudent(Into.e("(int)(2 + (2.0)*1L)")), is(INT));
    }

    @Test public void test103() {
      azzert.that(prudent(Into.e("(int)(2 + (2.0)*1L)==9.0")), is(BOOLEAN));
    }

    @Test public void test104() {
      azzert.that(prudent(Into.e("9*3.0+f()")), is(DOUBLE));
    }

    @Test public void test105() {
      azzert.that(prudent(Into.e("g()+f()")), is(ALPHANUMERIC));
    }

    @Test public void test106() {
      azzert.that(prudent(Into.e("f(g()+h(),f(2))")), is(NOTHING));
    }

    @Test public void test107() {
      azzert.that(prudent(Into.e("(((null)))")), is(NULL));
    }

    @Test public void test108() {
      azzert.that(prudent(Into.e("f()+null")), is(STRING));
    }

    @Test public void test109() {
      azzert.that(prudent(Into.e("(List)f()")), is(BAPTIZED));
    }

    @Test public void test110() {
      azzert.that(prudent(Into.e("new List<Integer>()")), is(NONNULL));
    }

    @Test public void test111() {
      azzert.that(prudent(Into.e("new Object()")), is(NONNULL));
    }

    @Test public void test112() {
      azzert.that(prudent(Into.e("new String(\"hello\")")), is(STRING));
    }

    @Test public void test113() {
      azzert.that(prudent(Into.e("\"a string\"")), is(STRING));
    }

    @Test public void test114() {
      azzert.that(prudent(Into.e("'a'")), is(CHAR));
    }

    @Test public void test115() {
      azzert.that(prudent(Into.e("5")), is(INT));
    }

    @Test public void test116() {
      azzert.that(prudent(Into.e("5L")), is(LONG));
    }

    @Test public void test117() {
      azzert.that(prudent(Into.e("5.0")), is(DOUBLE));
    }

    @Test public void test118() {
      azzert.that(prudent(Into.e("true")), is(BOOLEAN));
    }

    @Test public void test119() {
      azzert.that(prudent(Into.e("2+f()")), is(NUMERIC));
    }

    @Test public void test120() {
      azzert.that(prudent(Into.e("2%f()")), is(INT));
    }

    @Test public void test121() {
      azzert.that(prudent(Into.e("2<<f()")), is(INT));
    }

    @Test public void test122() {
      azzert.that(prudent(Into.e("f()<<2")), is(INTEGRAL));
    }

    @Test public void test123() {
      azzert.that(prudent(Into.e("f()||g()")), is(BOOLEAN));
    }

    @Test public void test124a() {
      azzert.that(prudent(Into.e("(char)x")), is(CHAR));
    }

    @Test public void test124b() {
      azzert.that(prudent(Into.e("(Character)x")), is(CHAR));
    }

    @Test public void test125a() {
      azzert.that(prudent(Into.e("(int)x")), is(INT));
    }

    @Test public void test125b() {
      azzert.that(prudent(Into.e("(Integer)x")), is(INT));
    }

    @Test public void test126a() {
      azzert.that(prudent(Into.e("(long)x")), is(LONG));
    }

    @Test public void test126b() {
      azzert.that(prudent(Into.e("(Long)x")), is(LONG));
    }

    @Test public void test127a() {
      azzert.that(prudent(Into.e("(double)x")), is(DOUBLE));
    }

    @Test public void test127b() {
      azzert.that(prudent(Into.e("(Double)x")), is(DOUBLE));
    }

    @Test public void test128a() {
      azzert.that(prudent(Into.e("(boolean)x")), is(BOOLEAN));
    }

    @Test public void test128b() {
      azzert.that(prudent(Into.e("(Boolean)x")), is(BOOLEAN));
    }

    @Test public void test129() {
      azzert.that(prudent(Into.e("(String)x")), is(STRING));
    }

    @Test public void test130() {
      azzert.that(prudent(Into.e("x++")), is(NUMERIC));
    }

    @Test public void test131() {
      azzert.that(prudent(Into.e("7++")), is(INT));
    }

    @Ignore("creates CharacterLiteral instead of PrefixExpression, need to figure out why") @Test public void test132() {
      azzert.that(prudent(Into.e("'a'--")), is(INT));
    }

    @Test public void test133() {
      azzert.that(prudent(Into.e("2L++")), is(LONG));
    }

    @Test public void test134() {
      azzert.that(prudent(Into.e("(-3.0)--")), is(DOUBLE));
    }
    
    @Test public void test135() {
      azzert.that(prudent(Into.e("a.toString()")), is(STRING));
    }
    
    @Test public void test136() {
      azzert.that(prudent(Into.e("a.fo()")), is(NOTHING));
    }
    
    @Test public void test137() {
      azzert.that(prudent(Into.e("f() ? 3 : 7")), is(INT));
    }
    
    @Test public void test138() {
      azzert.that(prudent(Into.e("f() ? 3L : 7")), is(LONG));
    }
    
    @Test public void test139() {
      azzert.that(prudent(Into.e("f() ? 3L : 7.")), is(DOUBLE));
    }
    
    @Test public void test140() {
      azzert.that(prudent(Into.e("f() ? 3L : 7.")), is(DOUBLE));
    }
    
    @Test public void test141() {
      azzert.that(prudent(Into.e("f() ? 'a' : 7.")), is(DOUBLE));
    }
    
    @Test public void test142() {
      azzert.that(prudent(Into.e("f() ? 'a' : 'b'")), is(CHAR));
    }
    
    @Test public void test143() {
      azzert.that(prudent(Into.e("f() ? \"abc\" : \"def\"")), is(STRING));
    }
    
    @Test public void test144() {
      azzert.that(prudent(Into.e("f() ? true : false")), is(BOOLEAN));
    }
    
    //perhaps this should return boolean?
    @Test public void test145() {
      azzert.that(prudent(Into.e("f() ? f() : false")), is(NOTHING));
    }
  }
}
