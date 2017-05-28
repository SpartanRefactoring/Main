package il.org.spartan.spartanizer.engine;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.type.*;
import static il.org.spartan.spartanizer.engine.type.Odd.Types.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Uncertain.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.traversal.Traversal.*;

/** unit tests for {@link __} , as well as tests for the types of certain
 * expression using {@link Axiom} .
 * @Since 2016-09
 * @author Niv Shalmon
 * @since Sep 7, 2016 */
@SuppressWarnings({ "javadoc", "static-method", "unused" })
public final class typeTest {
  /** Ideally this class is empty, but still {@link forget} d.
   * @author Yossi Gil */
  // @forget("Under construction")
  public static class NotWorkingYet {
    // All test work now
  }

  // @forget("Under construction")
  public static class Pending {
    // No tests are pending
  }

  public static class Working {
    private static final long LONG_MINUS_3L = -3L;
    private static final int __1 = 1;
    private static final int __12 = 1;
    private static final int __13 = 1;
    private static final int __14 = 1;
    private static final int __15 = 1;
    private static final long __1L = 1L;
    private static final long __2L = 2L;
    private static final int __3 = 3;
    private static final int __32 = 3;
    private static final int __33 = 3;
    /** Make sure the compiler cannot optimize this out */
    private byte b = (byte) (hashCode() ^ 0xDEADdeaf);
    private final boolean b1 = b > (byte) hashCode();
    private final boolean b2 = (b & (byte) hashCode() << 3) < 0;
    private char c = (char) (b << __2L);
    private final char c1 = (char) (hashCode() ^ hashCode() << 7);
    private final char c2 = (char) (c1 << 13 ^ hashCode());
    private double d = c1 / c2;
    private float f = (float) (0xCABAC0DAABBAL * c * d / b - (c1 ^ c2));
    private int i = (int) d;
    private long l = c2 + c1 * (b + i) << b;
    private short s = (short) ((i ^ l) * (1L * c1 ^ c2 << 0xF) / d);
    private final String str = "string";

    @Test public void assingment1() {
      azzert.that(of(into.e("x = 2")), is(NUMERIC));
    }
    @Test public void assingment2() {
      azzert.that(of(into.e("x = \"a string\"")), is(STRING));
    }
    @Test public void assingment3() {
      azzert.that(of(into.e("x = true")), is(BOOLEAN));
    }
    @Test public void assingment4() {
      azzert.that(of(into.e("x = new Float()")), is(NUMERIC));
    }
    @Test public void assingment5() {
      azzert.that(of(into.e("x = new String()")), is(STRING));
    }
    @Test public void assingment6() {
      azzert.that(of(into.e("x = new Object()")), is(baptize("Object")));
    }
    @Test public void axiomAssignment1() {
      azzert.that(Axiom.type(i = 2), is(INT));
    }
    @Test public void axiomAssignment2() {
      azzert.that(Axiom.type(b = 2), is(BYTE));
    }
    @Test public void axiomAssignment3() {
      azzert.that(Axiom.type(c = 2), is(CHAR));
    }
    @Test public void axiomAssignment4() {
      azzert.that(Axiom.type(s = 2), is(SHORT));
    }
    @Test public void axiomAssignment5() {
      azzert.that(Axiom.type(l = 2), is(LONG));
    }
    @Test public void axiomAssignment6() {
      azzert.that(Axiom.type(d = 2), is(DOUBLE));
    }
    @Test public void axiomAssignment7() {
      azzert.that(Axiom.type("abs"), is(STRING));
    }
    @Test public void axiomAssignment8() {
      azzert.that(Axiom.type(d = i = 2), is(DOUBLE));
    }
    @Test public void axiomAssignment9() {
      azzert.that(Axiom.type(i = (int) (d = 2)), is(INT));
    }
    @Test public void axiomBoolean1() {
      azzert.that(Axiom.type(true), is(BOOLEAN));
    }
    @Test public void axiomBoolean2() {
      azzert.that(Axiom.type(true || b1 && b2), is(BOOLEAN));
    }
    @Test public void axiomBoolean3() {
      azzert.that(Axiom.type(hashCode() > 6 && i != 14), is(BOOLEAN));
    }
    @Test public void axiomByte() {
      azzert.that(Axiom.type((byte) 1), is(BYTE));
    }
    @Test public void axiomChar1() {
      azzert.that(Axiom.type('a'), is(CHAR));
    }
    @Test public void axiomConditional01() {
      azzert.that(Axiom.type(b1 ? 3 : s), is(SHORT));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional02() {
      azzert.that(Axiom.type(b1 ? 3 : str), is(NOTHING));
    }
    @Test public void axiomConditional03() {
      azzert.that(Axiom.type(b1 ? s : 3), is(SHORT));
    }
    @Test public void axiomConditional04() {
      azzert.that(Axiom.type(b1 ? i : b), is(INT));
    }
    @Test public void axiomConditional05() {
      azzert.that(Axiom.type(b1 ? i : l), is(LONG));
    }
    @Test public void axiomConditional07() {
      azzert.that(Axiom.type(b1 ? f : s), is(FLOAT));
    }
    @Test public void axiomConditional08() {
      azzert.that(Axiom.type(b1 ? f : d), is(DOUBLE));
    }
    @Test public void axiomConditional09() {
      azzert.that(Axiom.type(b1 ? s : b), is(SHORT));
    }
    @Test public void axiomConditional10() {
      azzert.that(Axiom.type(b), is(BYTE));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional11() {
      azzert.that(Axiom.type(b1 ? b2 : f), is(NOTHING));
    }
    @Test public void axiomConditional12() {
      azzert.that(Axiom.type(b1 && b2), is(BOOLEAN));
    }
    @Test public void axiomConditional13() {
      azzert.that(Axiom.type(b1 ? str : ""), is(STRING));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional14() {
      azzert.that(Axiom.type(b1 ? str : b2), is(NOTHING));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional15() {
      azzert.that(Axiom.type(b1 ? c : b2), is(NOTHING));
    }
    @Test public void axiomConditional16() {
      azzert.that(Axiom.type(b1 ? c : b), is(INT));
    }
    @Test public void axiomConditional17() {
      azzert.that(Axiom.type(b1 ? c : s), is(INT));
    }
    @Test public void axiomConditional18() {
      azzert.that(Axiom.type(b1 ? 3L : s), is(LONG));
    }
    @Test public void axiomConditional19() {
      azzert.that(Axiom.type(b1 ? i : s), is(INT));
    }
    @Test public void axiomConditional20() {
      azzert.that(Axiom.type(!b1 ? s : 2147483647), is(INT));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional21() {
      azzert.that(Axiom.type(b1 ? i : null), is(NOTHING));
    }
    @Test @SuppressWarnings("boxing") public void axiomConditional22() {
      azzert.that(Axiom.type(b1 ? (Integer) i : (Integer) null), is(NOTHING));
    }
    @Test public void axiomConditional23() {
      azzert.that(Axiom.type(!b1 ? s : 'a'), is(INT));
    }
    @Test public void axiomDouble() {
      azzert.that(Axiom.type(7.), is(DOUBLE));
    }
    @Test public void axiomExpression1() {
      azzert.that(Axiom.type(__3 / 2. + 7), is(DOUBLE));
    }
    @Test public void axiomExpression10() {
      azzert.that(Axiom.type(9f / 9), is(FLOAT));
    }
    @Test public void axiomExpression11() {
      azzert.that(Axiom.type((float) 1 / (int) 1L), is(FLOAT));
    }
    @Test public void axiomExpression12() {
      azzert.that(Axiom.type(__1 * (float) 1 / 1L), is(FLOAT));
    }
    @Test public void axiomExpression13() {
      azzert.that(Axiom.type((float) 1 / (short) 1), is(FLOAT));
    }
    @Test public void axiomExpression14() {
      azzert.that(Axiom.type(__14 * __1L + (float) __15), is(FLOAT));
    }
    @Test public void axiomExpression15() {
      azzert.that(Axiom.type((char) __13 + (float) __12), is(FLOAT));
    }
    @Test public void axiomExpression2() {
      azzert.that(Axiom.type(3 / __2L + 7), is(LONG));
    }
    @Test public void axiomExpression3() {
      azzert.that(Axiom.type(16), is(INT));
    }
    @Test public void axiomExpression4() {
      azzert.that(Axiom.type(16L), is(LONG));
    }
    @Test public void axiomExpression5() {
      azzert.that(Axiom.type(-3.0), is(DOUBLE));
    }
    @Test public void axiomExpression6() {
      azzert.that(Axiom.type((2 * __32 / 4 + 1. - 5) % 4), is(DOUBLE));
    }
    @Test public void axiomExpression7() {
      azzert.that(Axiom.type((2 * __33 / 4 + 1 - 5) % 4), is(INT));
    }
    @Test public void axiomExpression8() {
      azzert.that(Axiom.type(LONG_MINUS_3L % 4), is(LONG));
    }
    @Test public void axiomExpression9() {
      azzert.that(Axiom.type(-0.022321428571428572), is(DOUBLE));
    }
    @Test public void axiomFloat() {
      azzert.that(Axiom.type(7f), is(FLOAT));
    }
    @Test public void axiomInt1() {
      azzert.that(Axiom.type(7), is(INT));
    }
    @Test public void axiomInt2() {
      azzert.that(Axiom.type(4 + 'a'), is(INT));
    }
    @Test public void axiomLong() {
      azzert.that(Axiom.type(7L), is(LONG));
    }
    @Test public void axiomNull() {
      azzert.that(Axiom.type((Integer) null), is(NOTHING));
    }
    @Test public void axiomShort() {
      azzert.that(Axiom.type((short) 3), is(SHORT));
    }
    @Test public void axiomString1() {
      azzert.that(Axiom.type("string"), is(STRING));
    }
    @Test public void axiomString2() {
      azzert.that(Axiom.type("string" + 9.0), is(STRING));
    }
    @Test public void axiomString3() {
      azzert.that(Axiom.type("string" + 'd'), is(STRING));
    }
    @Test public void axiomString4() {
      azzert.that(Axiom.type(Integer.toString(15)), is(STRING));
    }
    @Test public void basicExpression30() {
      azzert.that(of(into.e("+x")), is(NUMERIC));
    }
    @Test public void basicExpression31() {
      azzert.that(of(into.e("+x + +x")), is(NUMERIC));
    }
    @Test public void basicExpressions01() {
      azzert.that(of(into.e("2 + (2.0)*1L")), is(DOUBLE));
    }
    @Test public void basicExpressions02() {
      azzert.that(of(into.e("(int)(2 + (2.0)*1L)")), is(INT));
    }
    @Test public void basicExpressions03() {
      azzert.that(of(into.e("(int)(2 + (2.0)*1L)==9.0")), is(BOOLEAN));
    }
    @Test public void basicExpressions04() {
      azzert.that(of(into.e("9*3.0-f()")), is(DOUBLE));
    }
    @Test public void basicExpressions05() {
      azzert.that(of(into.e("g()+f()")), is(ALPHANUMERIC));
    }
    @Test public void basicExpressions06() {
      azzert.that(of(into.e("f(g()+h(),f(2))")), is(NOTHING));
    }
    @Test public void basicExpressions07() {
      azzert.that(of(into.e("f()+null")), is(STRING));
    }
    @Test public void basicExpressions08() {
      azzert.that(of(into.e("2+f()")), is(ALPHANUMERIC));
    }
    @Test public void basicExpressions09() {
      azzert.that(of(into.e("2%f()")), is(INTEGRAL));
    }
    @Test public void basicExpressions10() {
      azzert.that(of(into.e("2<<f()")), is(INT));
    }
    @Test public void basicExpressions11() {
      azzert.that(of(into.e("f()<<2")), is(INTEGRAL));
    }
    @Test public void basicExpressions12() {
      azzert.that(of(into.e("f()||g()")), is(BOOLEAN));
    }
    @Test public void basicExpressions13() {
      azzert.that(of(into.e("x++")), is(NUMERIC));
    }
    @Test public void basicExpressions18() {
      azzert.that(of(into.e("((short)1)+((short)2)")), is(INT));
    }
    @Test public void basicExpressions19() {
      azzert.that(of(into.e("((byte)1)+((byte)2)")), is(INT));
    }
    @Test public void basicExpressions20() {
      azzert.that(of(into.e("1f + 1")), is(FLOAT));
    }
    @Test public void basicExpressions21() {
      azzert.that(of(into.e("1f + 1l")), is(FLOAT));
    }
    @Test public void basicExpressions22() {
      azzert.that(of(into.e("1F + 'a'")), is(FLOAT));
    }
    @Test public void basicExpressions23() {
      azzert.that(of(into.e("1f + 1.")), is(DOUBLE));
    }
    @Test public void basicExpressions24() {
      azzert.that(of(into.e("1f / f()")), is(NUMERIC));
    }
    @Test public void basicExpressions25() {
      azzert.that(of(into.e("1+2+3l")), is(LONG));
    }
    @Test public void basicExpressions26() {
      azzert.that(of(into.e("1+2f+3l-5-4d")), is(DOUBLE));
    }
    @Test public void basicExpressions27() {
      azzert.that(of(into.e("1+2f+3l+f()-5-4d")), is(DOUBLE));
    }
    @Test public void basicExpressions28() {
      azzert.that(of(into.e("1+2f+3l+f()")), is(ALPHANUMERIC));
    }
    @Test public void basticExpression29() {
      final InfixExpression e = az.infixExpression(into.e("null+3"));
      azzert.that(of(e.getLeftOperand()), is(NULL));
      azzert.that(of(e.getRightOperand()), is(INT));
      azzert.that(of(e), is(STRING));
    }
    @Test public void BitwiseOperationsSemantics01() {
      azzert.that(Axiom.type(c1 | c2), is(INT));
    }
    @Test public void BitwiseOperationsSemantics02() {
      azzert.that(Axiom.type(b & c1), is(INT));
    }
    @Test public void BitwiseOperationsSemantics03() {
      azzert.that(Axiom.type(b1 | b2), is(BOOLEAN));
    }
    @Test public void BitwiseOperationsSemantics04() {
      azzert.that(Axiom.type(b1 ^ b2), is(BOOLEAN));
    }
    @Test public void cast01() {
      azzert.that(of(into.e("(List)f()")), is(baptize("List")));
    }
    @Test public void cast02() {
      azzert.that(of(into.e("(char)x")), is(CHAR));
    }
    @Test public void cast03() {
      azzert.that(of(into.e("(Character)x")), is(CHAR));
    }
    @Test public void cast04() {
      azzert.that(of(into.e("(int)x")), is(INT));
    }
    @Test public void cast05() {
      azzert.that(of(into.e("(Integer)x")), is(INT));
    }
    @Test public void cast06() {
      azzert.that(of(into.e("(long)x")), is(LONG));
    }
    @Test public void cast07() {
      azzert.that(of(into.e("(Long)x")), is(LONG));
    }
    @Test public void cast08() {
      azzert.that(of(into.e("(double)x")), is(DOUBLE));
    }
    @Test public void cast09() {
      azzert.that(of(into.e("(Double)x")), is(DOUBLE));
    }
    @Test public void cast10() {
      azzert.that(of(into.e("(boolean)x")), is(BOOLEAN));
    }
    @Test public void cast11() {
      azzert.that(of(into.e("(Boolean)x")), is(BOOLEAN));
    }
    @Test public void cast12() {
      azzert.that(of(into.e("(String)x")), is(STRING));
    }
    @Test public void cast13() {
      azzert.that(of(into.e("(byte)1")), is(BYTE));
    }
    @Test public void cast14() {
      azzert.that(of(into.e("(Byte)1")), is(BYTE));
    }
    @Test public void cast15() {
      azzert.that(of(into.e("(short)1")), is(SHORT));
    }
    @Test public void cast16() {
      azzert.that(of(into.e("(Short)1")), is(SHORT));
    }
    @Test public void cast17() {
      azzert.that(of(into.e("(float)1")), is(FLOAT));
    }
    @Test public void cast18() {
      azzert.that(of(into.e("(Float)1")), is(FLOAT));
    }
    @Test public void cast19() {
      azzert.that(of(into.e("(float)1d")), is(FLOAT));
    }
    @Test public void cast20() {
      azzert.that(of(into.e("(Integer)null")), is(NULL));
    }
    @Test public void cast21() {
      azzert.that(of(into.e("(Integer)((null))")), is(NULL));
    }
    @Test public void conditional01() {
      azzert.that(of(into.e("f() ? 3 : 7")), is(INT));
    }
    @Test public void conditional02() {
      azzert.that(of(into.e("f() ? 3L : 7")), is(LONG));
    }
    @Test public void conditional03() {
      azzert.that(of(into.e("f() ? 3L : 7.")), is(DOUBLE));
    }
    @Test public void conditional04() {
      azzert.that(of(into.e("f() ? 3 : (short)7")), is(SHORT));
    }
    @Test public void conditional05() {
      azzert.that(of(into.e("f() ? 'a' : 7.")), is(DOUBLE));
    }
    @Test public void conditional06() {
      azzert.that(of(into.e("f() ? 'a' : 'b'")), is(CHAR));
    }
    @Test public void conditional07() {
      azzert.that(of(into.e("f() ? \"abc\" : \"def\"")), is(STRING));
    }
    @Test public void conditional08() {
      azzert.that(of(into.e("f() ? true : false")), is(BOOLEAN));
    }
    @Test public void conditional09() {
      azzert.that(of(into.e("f() ? f() : false")), is(NOTHING));
    }
    @Test public void conditional10() {
      azzert.that(of(into.e("f() ? f() : 2")), is(NOTHING));
    }
    @Test public void conditional11() {
      azzert.that(of(into.e("f() ? f() : 2l")), is(NOTHING));
    }
    @Test public void conditional12() {
      azzert.that(of(into.e("f() ? 2. : g()")), is(NOTHING));
    }
    @Test public void conditional13() {
      azzert.that(of(into.e("f() ? 2 : 2%f()")), is(INTEGRAL));
    }
    @Test public void conditional14() {
      azzert.that(of(into.e("f() ? x : 'a'")), is(NOTHING));
    }
    @Test public void conditional15() {
      azzert.that(of(into.e("f() ? x : g()")), is(NOTHING));
    }
    @Test public void conditional16() {
      azzert.that(of(into.e("f() ? \"a\" : h()")), is(NOTHING));
    }
    @Test public void conditional17() {
      azzert.that(of(into.e("s.equals(532)?y(2)+10:r(3)-6")), is(NOTHING));
    }
    @Test public void conditional18() {
      azzert.that(of(into.e("b ? y(2)+10 : x-6")), is(NOTHING));
    }
    @Test public void conditional19() {
      azzert.that(of(into.e("b ? \"\" : 7")), is(NOTHING));
    }
    @Test public void conditional20() {
      azzert.that(of(into.e("b ? (byte)3 : 7")), is(INT));
    }
    @Test public void conditional21() {
      azzert.that(of(into.e("b ? (short)3 : 7")), is(SHORT));
    }
    @Test public void conditional22() {
      azzert.that(of(into.e("b ? (short)3 : 7l")), is(LONG));
    }
    @Test public void conditional23() {
      azzert.that(of(into.e("b ? (short)3 : 2147483647")), is(INT));
    }
    @Test public void constructors01() {
      azzert.that(of(into.e("new List<Integer>()")), is(baptize("List<Integer>")));
      azzert.assertNotEquals(of(into.e("new List<Integer>()")), baptize("List"));
    }
    @Test public void constructors02() {
      azzert.that(of(into.e("new Object()")), is(baptize("Object")));
    }
    @Test public void constructors03() {
      azzert.that(of(into.e("new String(\"hello\")")), is(STRING));
    }
    @Test public void constructors04() {
      azzert.that(of(into.e("new Byte()")), is(BYTE));
    }
    @Test public void constructors05() {
      azzert.that(of(into.e("new Double()")), is(DOUBLE));
    }
    @Test public void context01() {
      azzert.that(of(findFirst.ifStatement(into.s("{if(f()) return x; return y;}")).getExpression()), is(BOOLEAN));
    }
    @Test public void context02() {
      azzert.that(of(az.prefixExpression(into.e("++x")).getOperand()), is(NUMERIC));
    }
    @Test public void context03() {
      azzert.that(of(az.prefixExpression(into.e("--x")).getOperand()), is(NUMERIC));
    }
    @Test public void context04() {
      azzert.that(of(az.prefixExpression(into.e("-x")).getOperand()), is(NUMERIC));
    }
    @Test public void context05() {
      azzert.that(of(az.prefixExpression(into.e("!x")).getOperand()), is(BOOLEAN));
    }
    @Test public void context06() {
      azzert.that(of(az.prefixExpression(into.e("~x")).getOperand()), is(INTEGRAL));
    }
    @Test public void context07() {
      azzert.that(of(az.prefixExpression(into.e("+x")).getOperand()), is(NUMERIC));
    }
    @Test public void context08() {
      azzert.that(of(az.postfixExpression(into.e("x++")).getOperand()), is(NUMERIC));
    }
    @Test public void context09() {
      azzert.that(of(az.postfixExpression(into.e("x--")).getOperand()), is(NUMERIC));
    }
    @Test public void context10() {
      azzert.that(of(((ArrayAccess) into.e("arr[x]")).getIndex()), is(INTEGRAL));
    }
    @Test public void context11() {
      azzert.that(of(((ArrayAccess) into.e("arr[((x))]")).getIndex()), is(INTEGRAL));
    }
    @Test public void context12() {
      final InfixExpression e = az.infixExpression(into.e("x <7"));
      azzert.that(of(e.getLeftOperand()), is(NUMERIC));
      azzert.that(of(e.getRightOperand()), is(INT));
    }
    @Test public void context13() {
      final InfixExpression e = az.infixExpression(into.e("x == (byte)7"));
      azzert.that(of(e.getLeftOperand()), is(NOTHING));
      azzert.that(of(e.getRightOperand()), is(BYTE));
    }
    @Test public void context14() {
      final InfixExpression e = az.infixExpression(into.e("x != 'c'"));
      azzert.that(of(e.getLeftOperand()), is(NOTHING));
      azzert.that(of(e.getRightOperand()), is(CHAR));
    }
    @Test public void context15() {
      final InfixExpression e = az.infixExpression(into.e("y> 7.3"));
      azzert.that(of(e.getLeftOperand()), is(NUMERIC));
      azzert.that(of(e.getRightOperand()), is(DOUBLE));
    }
    @Test public void context16() {
      final InfixExpression e = az.infixExpression(into.e("x | 7l"));
      azzert.that(of(e.getLeftOperand()), is(BOOLEANINTEGRAL));
      azzert.that(of(e.getRightOperand()), is(LONG));
    }
    @Test public void context17() {
      final InfixExpression e = az.infixExpression(into.e("x & y"));
      azzert.that(of(e.getLeftOperand()), is(BOOLEANINTEGRAL));
      azzert.that(of(e.getRightOperand()), is(BOOLEANINTEGRAL));
    }
    @Test public void context18() {
      final InfixExpression e = az.infixExpression(into.e("x + \"y\""));
      azzert.that(of(e.getLeftOperand()), is(ALPHANUMERIC));
      azzert.that(of(e.getRightOperand()), is(STRING));
    }
    @Test public void context19() {
      final InfixExpression e = az.infixExpression(into.e("x - 9f"));
      azzert.that(of(e.getLeftOperand()), is(NUMERIC));
      azzert.that(of(e.getRightOperand()), is(FLOAT));
    }
    @Test @SuppressWarnings("unchecked") public void context20() {
      final ForStatement fs = findFirst.forStatement(into.s("for(int i = 0;x;++i) somthing();"));
      azzert.that(of(fs.getExpression()), is(BOOLEAN));
      azzert.that(of((Expression) the.firstOf(fs.initializers())), is(INT));
      azzert.that(of((Expression) the.firstOf(fs.updaters())), is(NUMERIC));
    }
    @Test public void context22() {
      final AssertStatement as = findFirst.assertStatement(into.s("assert x : \"message\";"));
      azzert.that(of(as.getExpression()), is(BOOLEAN));
      azzert.that(of(as.getMessage()), is(STRING));
    }
    @Test public void InDecreamentSemantics01() {
      azzert.that(Axiom.type(i++), is(INT));
    }
    @Test public void InDecreamentSemantics02() {
      azzert.that(Axiom.type(l--), is(LONG));
    }
    @Test public void inDecreamentSemantics03() {
      azzert.that(Axiom.type(++s), is(SHORT));
    }
    @Test public void InDecreamentSemantics04() {
      azzert.that(Axiom.type(d++), is(DOUBLE));
    }
    @Test public void InDecreamentSemantics05() {
      azzert.that(Axiom.type(--f), is(FLOAT));
    }
    @Test public void InDecreamentSemantics06() {
      byte x = 0;
      azzert.that(Axiom.type(--x), is(BYTE));
    }
    @Test public void InDecreamentSemantics07() {
      char x = 0;
      azzert.that(Axiom.type(--x), is(CHAR));
    }
    @Test public void literal01() {
      azzert.that(of(into.e("3")), is(INT));
    }
    @Test public void literal02() {
      azzert.that(of(into.e("3l")), is(LONG));
    }
    @Test public void literal03() {
      azzert.that(of(into.e("3L")), is(LONG));
    }
    @Test public void literal04() {
      azzert.that(of(into.e("3d")), is(DOUBLE));
    }
    @Test public void literal05() {
      azzert.that(of(into.e("3D")), is(DOUBLE));
    }
    @Test public void literal06() {
      azzert.that(of(into.e("3.0d")), is(DOUBLE));
    }
    @Test public void literal07() {
      azzert.that(of(into.e("3.02D")), is(DOUBLE));
    }
    @Test public void Literal08() {
      azzert.that(of(into.e("3.098")), is(DOUBLE));
    }
    @Test public void literal09() {
      azzert.that(of(into.e("3f")), is(FLOAT));
    }
    @Test public void literal10() {
      azzert.that(of(into.e("3.f")), is(FLOAT));
    }
    @Test public void literal11() {
      azzert.that(of(into.e("3.0f")), is(FLOAT));
    }
    @Test public void literals12() {
      azzert.that(of(into.e("null")), is(NULL));
    }
    @Test public void literals13() {
      azzert.that(of(into.e("(((null)))")), is(NULL));
    }
    @Test public void literals14() {
      azzert.that(of(into.e("\"a string\"")), is(STRING));
    }
    @Test public void literals15() {
      azzert.that(of(into.e("'a'")), is(CHAR));
    }
    @Test public void literals16() {
      azzert.that(of(into.e("true")), is(BOOLEAN));
    }
    @Test public void makeSureIUnderstandSemanticsOfShift() {
      azzert.that(Axiom.type((short) 1 << 1L), is(INT));
    }
    @Test public void methods1() {
      azzert.that(of(into.e("a.toString()")), is(STRING));
    }
    @Test public void methods2() {
      azzert.that(of(into.e("a.fo()")), is(NOTHING));
    }
    @Test public void methods3() {
      azzert.that(of(into.e("toString()")), is(STRING));
    }
    @Test public void methods4() {
      azzert.that(of(into.e("toString(x,y)")), is(NOTHING));
    }
    @Test public void unaryPlusMinusSemantics01() {
      azzert.that(Axiom.type(i), is(INT));
    }
    @Test public void unaryPlusMinusSemantics02() {
      azzert.that(Axiom.type(-l), is(LONG));
    }
    @Test public void unaryPlusMinusSemantics03() {
      azzert.that(Axiom.type(i), is(INT));
    }
    @Test public void unaryPlusMinusSemantics04() {
      azzert.that(Axiom.type(d), is(DOUBLE));
    }
    @Test public void unaryPlusMinusSemantics05() {
      azzert.that(Axiom.type(-f), is(FLOAT));
    }
    @Test public void unaryPlusMinusSemantics06() {
      azzert.that(Axiom.type(b), is(BYTE));
    }
    @Test public void unaryPlusMinusSemantics07() {
      azzert.that(Axiom.type(-b), is(INT));
    }
    @Test public void unaryPlusMinusSemantics08() {
      azzert.that(Axiom.type(-0), is(INT));
    }
    @Test public void unaryPlusMinusSemantics09() {
      azzert.that(Axiom.type(-c1), is(INT));
    }
    @Test public void variableDeclarationStatement01() {
      final Statement e = into.s("int x = f();");
      azzert.that(of(extract.fragments(e).get(0).getInitializer()), is(INT));
    }
    @Test public void variableDeclarationStatement02() {
      final Statement e = into.s("Long x = 3.0;");
      azzert.that(of(extract.fragments(e).get(0).getInitializer()), is(DOUBLE));
    }
    @Test public void variableDeclarationStatement03() {
      final Statement e = into.s("Long x = 3.0;");
      azzert.that(of(extract.fragments(e).get(0).getInitializer()), is(DOUBLE));
    }
  }
}
