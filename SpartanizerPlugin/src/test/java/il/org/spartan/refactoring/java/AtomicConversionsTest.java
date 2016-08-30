package il.org.spartan.refactoring.java;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class AtomicConversionsTest {
  @Test public void floatAndFloat() {
    azzert.nay(atomic.isDouble(1L));
    azzert.aye(atomic.isLong(1L));
    azzert.aye(atomic.isFloat(2F));
    float f = 3.1F;
    assert f != 0;
    azzert.aye(atomic.isFloat(2F + f));
    azzert.aye(atomic.isFloat(1L + 2F));
  }

  @Test public void charAndChar() {
    char c = 'c';
    assert c != 0;
    azzert.nay(atomic.isDouble(c));
    azzert.nay(atomic.isLong(c));
    azzert.nay(atomic.isFloat(c));
    azzert.nay(atomic.isInt(c));
    azzert.aye(atomic.isChar(c));
    azzert.nay(atomic.isChar(c + 'b'));
    azzert.nay(atomic.isFloat(c + "a"));
  }

  @Test public void mod() {
    azzert.aye(atomic.isLong(l / i));
    azzert.aye(atomic.isLong(l + i));
    azzert.aye(atomic.isLong(l % i));
    azzert.aye(atomic.isLong(i % l));
  }

  private final int i = hashCode();
  private final long l = 1L * (i + "").hashCode() * new Object().hashCode();

  @Test public void shift() {
    azzert.aye(atomic.isLong(l / i));
    azzert.aye(atomic.isLong(l + i));
    azzert.aye(atomic.isLong(l % i));
    azzert.aye(atomic.isLong(i % l));
  }
}
