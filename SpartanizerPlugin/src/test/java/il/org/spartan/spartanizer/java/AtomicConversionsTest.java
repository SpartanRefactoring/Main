package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc" }) //
public class AtomicConversionsTest {
  private byte b = (byte) hashCode();
  private final boolean b1 = getClass().equals(getClass());
  private final boolean b2 = !b1 | b << b == (b & 1);
  private char c = 'c';
  private double d = Math.sin(b * c);
  private float f = (float) Math.cos(d);
  private int i = c ^ hashCode() << (b & 0xF);
  private final long l = 1L * (i + "").hashCode() * new Object().hashCode();
  private short s = (short) (i * (l % i * (b + c)));
  private final Object o = getClass().getClassLoader().getClass();
  private final String S = toString();

  @Test public void booleans() {
    azzert.aye(atomic.isBoolean(b1));
    azzert.aye(atomic.isBoolean(b2));
    azzert.nay(atomic.isBoolean(c));
    azzert.nay(atomic.isBoolean(d));
    azzert.nay(atomic.isBoolean(s));
    azzert.nay(atomic.isBoolean(b));
    azzert.nay(atomic.isBoolean(i));
    azzert.nay(atomic.isBoolean(d));
    azzert.nay(atomic.isBoolean(f));
    azzert.nay(atomic.isBoolean(S));
    azzert.nay(atomic.isBoolean(o));
    azzert.aye(atomic.isBoolean(b1 && b2));
    azzert.aye(atomic.isBoolean(b1 != b2));
    azzert.aye(atomic.isBoolean(b1 || b2));
  }

  @Test public void booleansAsSemiIntegers() {
    azzert.aye(atomic.isBoolean(b1 & b2));
    azzert.aye(atomic.isBoolean(b1 | b2));
    azzert.aye(atomic.isBoolean(b1 ? b2 : !b2));
    azzert.aye(atomic.isBoolean(b1 | b2 & (b2^b1)));
    azzert.aye(atomic.isBoolean(b1 ^ b2));
  }

  @Test public void charAndChar() {
    azzert.nay(atomic.isDouble(c));
    azzert.nay(atomic.isLong(c));
    azzert.nay(atomic.isFloat(c));
    azzert.nay(atomic.isInt(c));
    azzert.aye(atomic.isChar(c));
    azzert.nay(atomic.isChar(c + 'b'));
    azzert.nay(atomic.isFloat(c + "a"));
  }

  @Test public void floatAndFloat() {
    azzert.nay(atomic.isDouble(1L));
    azzert.aye(atomic.isLong(1L));
    azzert.aye(atomic.isFloat(2F));
    azzert.aye(atomic.isFloat(2F + f));
    azzert.aye(atomic.isFloat(1L + 2F));
  }

  @Test public void mod() {
    azzert.aye(atomic.isLong(l / i));
    azzert.aye(atomic.isLong(l + i));
    azzert.aye(atomic.isLong(l % i));
    azzert.aye(atomic.isLong(i % l));
  }

  @Test public void preIncrement() {
    azzert.that(PrudentType.axiom(++d), is(PrudentType.DOUBLE));
    azzert.that(PrudentType.axiom(++f), is(PrudentType.FLOAT));
    azzert.that(PrudentType.axiom(++b), is(PrudentType.BYTE));
    azzert.that(PrudentType.axiom(++c), is(PrudentType.CHAR));
    azzert.that(PrudentType.axiom(++s), is(PrudentType.SHORT));
    azzert.that(PrudentType.axiom(++i), is(PrudentType.INT));
  }

  @Test public void shift2() {
    azzert.aye(atomic.isByte(b));
    azzert.nay(atomic.isByte(b << l));
    azzert.nay(atomic.isShort(b << l));
    azzert.aye(atomic.isShort(s));
    azzert.aye(atomic.isInt(b << l));
    azzert.nay(atomic.isChar(c << l));
    azzert.nay(atomic.isShort(c << l));
    azzert.aye(atomic.isInt(c << i));
    azzert.aye(atomic.isLong(c << l));
    azzert.aye(atomic.isInt(c << i));
    azzert.aye(atomic.isInt(i << l));
    azzert.aye(atomic.isLong(l << l));
  }

  @Test public void shiftByByte() {
    azzert.aye(atomic.isInt(b << b));
    azzert.aye(atomic.isInt(s << b));
    azzert.aye(atomic.isInt(c << b));
    azzert.aye(atomic.isInt(i << b));
  }

  @Test public void shiftByChar() {
    azzert.aye(atomic.isInt(b << c));
    azzert.aye(atomic.isInt(s << c));
    azzert.aye(atomic.isInt(c << c));
    azzert.aye(atomic.isInt(i << c));
  }

  @Test public void shiftByInt() {
    azzert.aye(atomic.isInt(b << i));
    azzert.aye(atomic.isInt(s << i));
    azzert.aye(atomic.isInt(c << i));
    azzert.aye(atomic.isInt(i << i));
  }

  @Test public void shiftByLong() {
    azzert.aye(atomic.isInt(b << l));
    azzert.aye(atomic.isInt(s << l));
    azzert.aye(atomic.isInt(c << l));
    azzert.aye(atomic.isInt(i << l));
  }

  @Test public void shiftByShort() {
    azzert.aye(atomic.isInt(b << s));
    azzert.aye(atomic.isInt(s << s));
    azzert.aye(atomic.isInt(c << s));
    azzert.aye(atomic.isInt(i << s));
  }

  @Test public void shiftOfLong() {
    azzert.aye(atomic.isLong(l << b));
    azzert.aye(atomic.isLong(l << c));
    azzert.aye(atomic.isLong(l << s));
    azzert.aye(atomic.isLong(l << i));
    azzert.aye(atomic.isLong(l << l));
  }

  @Test public void strings() {
    azzert.aye(atomic.isString("" + d));
    azzert.aye(atomic.isDouble(d + f));
    azzert.nay(atomic.isString(d + f));
    azzert.aye(atomic.isString("" + d + f));
    azzert.aye(atomic.isLong(l << s));
    azzert.aye(atomic.isLong(l << i));
    azzert.aye(atomic.isLong(l << l));
  }
}
