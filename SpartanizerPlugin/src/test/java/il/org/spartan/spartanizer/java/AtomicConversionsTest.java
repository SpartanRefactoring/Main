package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings("ALL")
public final class AtomicConversionsTest {
  private byte b = (byte) hashCode();
  private final boolean b1 = getClass().equals(getClass());
  private final boolean b2 = !b1 | b << b == (b & 1);
  private char c = 'c';
  private double d = Math.sin(b * c);
  private float f = (float) Math.cos(d);
  private int i = c ^ hashCode() << (b & 0xF);
  private final long l = new Object().hashCode() * (long) (i + "").hashCode();
  private short s = (short) (i * (b + c) % i);
  private final Object o = getClass().getClassLoader().getClass();
  private final String S = toString();

  @Test public void booleans() {
    assert atomic.isBoolean(b1);
    assert atomic.isBoolean(b2);
    assert !atomic.isBoolean(c);
    assert !atomic.isBoolean(d);
    assert !atomic.isBoolean(s);
    assert !atomic.isBoolean(b);
    assert !atomic.isBoolean(i);
    assert !atomic.isBoolean(d);
    assert !atomic.isBoolean(f);
    assert !atomic.isBoolean(S);
    assert !atomic.isBoolean(o);
    assert atomic.isBoolean(b1 && b2);
    assert atomic.isBoolean(b1 != b2);
    assert atomic.isBoolean(b1 || b2);
  }

  @Test public void booleansAsSemiIntegers() {
    assert atomic.isBoolean(b1 & b2);
    assert atomic.isBoolean(b1 | b2);
    assert atomic.isBoolean(b1 ? b2 : b1 ^ !b2 == b1);
    assert atomic.isBoolean(b1 | b2 & (b1 ^ b2));
    assert atomic.isBoolean(b1 ^ b2);
  }

  @Test public void charAndChar() {
    assert !atomic.isDouble(c);
    assert !atomic.isLong(c);
    assert !atomic.isFloat(c);
    assert !atomic.isInt(c);
    assert atomic.isChar(c);
    assert !atomic.isChar(c + 'b');
    assert !atomic.isFloat(c + "a");
  }

  @Test public void floatAndFloat() {
    assert !atomic.isDouble(1L);
    assert atomic.isLong(1L);
    assert atomic.isFloat(2F);
    assert atomic.isFloat(2F + f);
    assert atomic.isFloat(1L + 2F);
  }

  @Test public void mod() {
    assert atomic.isLong(l / i);
    assert atomic.isLong(l + i);
    assert atomic.isLong(l % i);
    assert atomic.isLong(i % l);
  }

  @Test public void preIncrement() {
    azzert.that(type.Axiom.type(++d), is(type.Primitive.Certain.DOUBLE));
    azzert.that(type.Axiom.type(++f), is(type.Primitive.Certain.FLOAT));
    azzert.that(type.Axiom.type(++b), is(type.Primitive.Certain.BYTE));
    azzert.that(type.Axiom.type(++c), is(type.Primitive.Certain.CHAR));
    azzert.that(type.Axiom.type(++s), is(type.Primitive.Certain.SHORT));
    azzert.that(type.Axiom.type(++i), is(type.Primitive.Certain.INT));
  }

  @Test public void shift2() {
    assert atomic.isByte(b);
    assert !atomic.isByte(b << l);
    assert !atomic.isShort(b << l);
    assert atomic.isShort(s);
    assert atomic.isInt(b << l);
    assert !atomic.isChar(c << l);
    assert !atomic.isShort(c << l);
    assert atomic.isInt(c << i);
    assert atomic.isLong(c << l);
    assert atomic.isInt(c << i);
    assert atomic.isInt(i << l);
    assert atomic.isLong(l << l);
  }

  @Test public void shiftByByte() {
    assert atomic.isInt(b << b);
    assert atomic.isInt(s << b);
    assert atomic.isInt(c << b);
    assert atomic.isInt(i << b);
  }

  @Test public void shiftByChar() {
    assert atomic.isInt(b << c);
    assert atomic.isInt(s << c);
    assert atomic.isInt(c << c);
    assert atomic.isInt(i << c);
  }

  @Test public void shiftByInt() {
    assert atomic.isInt(b << i);
    assert atomic.isInt(s << i);
    assert atomic.isInt(c << i);
    assert atomic.isInt(i << i);
  }

  @Test public void shiftByLong() {
    assert atomic.isInt(b << l);
    assert atomic.isInt(s << l);
    assert atomic.isInt(c << l);
    assert atomic.isInt(i << l);
  }

  @Test public void shiftByShort() {
    assert atomic.isInt(b << s);
    assert atomic.isInt(s << s);
    assert atomic.isInt(c << s);
    assert atomic.isInt(i << s);
  }

  @Test public void shiftOfLong() {
    assert atomic.isLong(l << b);
    assert atomic.isLong(l << c);
    assert atomic.isLong(l << s);
    assert atomic.isLong(l << i);
    assert atomic.isLong(l << l);
  }

  @Test public void strings() {
    assert atomic.isString(d + "");
    assert atomic.isDouble(d + f);
    assert !atomic.isString(d + f);
    assert atomic.isString(d + "");
    assert atomic.isLong(l << s);
    assert atomic.isLong(l << i);
    assert atomic.isLong(l << l);
  }
}
