package fluent.ly;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fluent.ly.___.Bug.Variant;
import fluent.ly.___.Bug.Assertion;
import fluent.ly.___.Bug.Assertion.Invariant;
import fluent.ly.___.Bug.Assertion.Reachability;
import fluent.ly.___.Bug.Assertion.Value;
import fluent.ly.___.Bug.Assertion.Value.NonNull;
import fluent.ly.___.Bug.Assertion.Value.Numerical;
import fluent.ly.___.Bug.Assertion.Value.Numerical.Negative;
import fluent.ly.___.Bug.Assertion.Value.Numerical.NonNegative;
import fluent.ly.___.Bug.Assertion.Value.Numerical.NonPositive;
import fluent.ly.___.Bug.Assertion.Value.Numerical.Positive;
import fluent.ly.___.Bug.Assertion.Variant.Initial;
import fluent.ly.___.Bug.Assertion.Variant.Nondecreasing;
import fluent.ly.___.Bug.Assertion.Variant.Underflow;
import fluent.ly.___.Bug.Assertion.Variant.Initial;
import fluent.ly.___.Bug.Contract;
import fluent.ly.___.Bug.Contract.Postcondition;
import fluent.ly.___.Bug.Contract.Precondition;

@SuppressWarnings("static-method")
public class BugTest {
  @Test public void ensure() {
    ___.ensure(true);
    try {
      ___.ensure(false);
    } catch (final Contract.Postcondition e) {
      assertEquals("", e.getMessage());
    }
    try {
      ___.ensure(false, "ensure");
    } catch (final Contract.Postcondition e) {
      assertEquals("ensure", e.getMessage());
    }
    try {
      ___.ensure(false, "ensure %s message %s", "this", "now");
    } catch (final Contract.Postcondition e) {
      assertEquals("ensure this message now", e.getMessage());
    }
  }
  @Test public void negative() {
    ___.negative(-1);
    ___.negative(-2);
    ___.negative(-0.3);
    try {
      ___.negative(0);
    } catch (final Numerical.Negative ¢) {
      assertEquals("Found 0 while expecting a negative integer.", ¢.getMessage());
    }
    try {
      ___.negative(0.0);
    } catch (final Numerical.Negative ¢) {
      assertEquals("Found 0.00000 while expecting a negative number.", ¢.getMessage());
    }
    try {
      ___.negative(-1);
    } catch (final Numerical.Negative ¢) {
      assertEquals("Found -1 while expecting a negative integer.", ¢.getMessage());
    }
    try {
      ___.negative(-1.0);
    } catch (final Numerical.Negative ¢) {
      assertEquals("Found -1.00000 while expecting a negative number.", ¢.getMessage());
    }
  }
  @Test public void nonnegative() {
    ___.nonnegative(1);
    ___.nonnegative(2);
    ___.nonnegative(0);
    ___.nonnegative(0.3);
    ___.nonnegative(0.0);
    try {
      ___.nonnegative(1);
    } catch (final Numerical.NonNegative ¢) {
      assertEquals("Found -1 while expecting a negative integer.", ¢.getMessage());
    }
    try {
      ___.nonnegative(1.0);
    } catch (final Numerical.NonNegative ¢) {
      assertEquals("Found -1.00000 while expecting a negative number.", ¢.getMessage());
    }
  }
  @Test public void NonNull() {
    assert new Object() != null;
    try {
      assert null != null;
    } catch (final AssertionError ¢) {
      assertEquals(null, ¢.getMessage());
    }
    try {
      assert "NonNull" != null : null;
    } catch (final Value.NonNull e) {
      assertEquals("NonNull", e.getMessage());
    }
    try {
      ___.nonNull(null, "NonNull %s message %s", "this", "now");
    } catch (final Value.NonNull e) {
      assertEquals("NonNull this message now", e.getMessage());
    }
  }
  @Test public void nonpositive() {
    ___.nonpositive(-1);
    ___.nonpositive(-2);
    ___.nonpositive(-0.3);
    ___.nonpositive(0);
    ___.nonpositive(0.0);
    try {
      ___.nonpositive(-1);
    } catch (final Numerical.NonPositive ¢) {
      assertEquals("Found -1 while expecting a nonpositive integer.", ¢.getMessage());
    }
    try {
      ___.nonpositive(-1.0);
    } catch (final Numerical.NonPositive ¢) {
      assertEquals("Found -1.00000 while expecting a nonpositive number.", ¢.getMessage());
    }
  }
  @Test public void positive() {
    ___.positive(1);
    ___.positive(2);
    ___.positive(0.3);
    try {
      ___.positive(0);
    } catch (final Numerical.Positive ¢) {
      assertEquals("Found 0 while expecting a positive integer.", ¢.getMessage());
    }
    try {
      ___.positive(0.0);
    } catch (final Numerical.Positive ¢) {
      assertEquals("Found 0.00000 while expecting a positive number.", ¢.getMessage());
    }
    try {
      ___.positive(-1);
    } catch (final Numerical.Positive ¢) {
      assertEquals("Found -1 while expecting a positive integer.", ¢.getMessage());
    }
    try {
      ___.positive(-1.0);
    } catch (final Numerical.Positive ¢) {
      assertEquals("Found -1.00000 while expecting a positive number.", ¢.getMessage());
    }
  }
  @Test public void require() {
    ___.require(true);
    try {
      ___.require(false);
    } catch (final Contract.Precondition ¢) {
      assertEquals("", ¢.getMessage());
    }
    try {
      ___.require(false, "requireMessage");
    } catch (final Contract.Precondition ¢) {
      assertEquals("requireMessage", ¢.getMessage());
    }
    try {
      ___.require(false, "require %s message %s", "this", "now");
    } catch (final Contract.Precondition ¢) {
      assertEquals("require this message now", ¢.getMessage());
    }
  }
  @Test(expected = ___.Bug.class) public void requireBug() {
    ___.require(false);
  }
  @Test(expected = Contract.Precondition.class) public void requirePrecondition() {
    ___.require(false);
  }
  @Test public void sure() {
    ___.sure(true);
    try {
      ___.sure(false);
    } catch (final Assertion.Invariant e) {
      assertEquals("", e.getMessage());
    }
    try {
      ___.sure(false, "sure");
    } catch (final Assertion.Invariant e) {
      assertEquals("sure", e.getMessage());
    }
    try {
      ___.sure(false, "sure %s message %s", "this", "now");
    } catch (final Assertion.Invariant e) {
      assertEquals("sure this message now", e.getMessage());
    }
  }
  @Test public void unreachable() {
    try {
      ___.unreachable();
    } catch (final Assertion.Reachability ¢) {
      assertEquals("", ¢.getMessage());
    }
    try {
      ___.unreachable("unreachable message");
    } catch (final Assertion.Reachability ¢) {
      assertEquals("unreachable message", ¢.getMessage());
    }
    try {
      ___.unreachable("unreachable %s message %s", "this", "now");
    } catch (final Assertion.Reachability ¢) {
      assertEquals("unreachable this message now", ¢.getMessage());
    }
  }
  @Test public void variant() {
    {
      final Variant v = new Variant(10);
      assertEquals(10, v.value());
      v.check(9);
      v.check(8);
      v.check(4);
      v.check(2);
      v.check(1);
      v.check(0);
      assertEquals(0, v.value());
    }
    try {
      forget.it(new Variant(-1));
    } catch (final Initial e) {
      assertEquals("Initial variant value (-1) is negative", e.getMessage());
    }
    try {
      final Variant v = new Variant(10);
      v.check(8);
      v.check(9);
    } catch (final Nondecreasing ¢) {
      assertEquals("New variant value (9) should be less than previous value (8)", ¢.getMessage());
    }
    try {
      final Variant v = new Variant(10);
      v.check(8);
      v.check(-2);
    } catch (final Underflow ¢) {
      assertEquals("New variant value (-2) is negative", ¢.getMessage());
    }
  }
}