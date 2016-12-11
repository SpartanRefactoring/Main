package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Unit tests for {@link MethodInvocationAssertTrueFalse} 
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue922 {
  private static Object object() {
    return new Object();
  }

  private int $0() {
    return 0;
  }

  private int $1() {
    return 1;
  }

  private boolean $false() {
    return false;
  }

  private Object $null() {
    return null;
  }

  private boolean $true() {
    return true;
  }

  private Object sameSomeObject() {
    return this;
  }

  /** if fails, suite did not compile... */
  @Test public void Z$000() {
    new Object().hashCode();
  }

  /** if fails, assertions do not work */
  @Test public void Z$010() {
    assert null == null : "This assert must never fail";
  }

  /** if fails, enable assertions (flag '-va') to the JVM. --yg In Windows it
   * worked for me by adding '-ea' flag in the run configurations -> VM
   * arguments. --or */
  @Test(expected = AssertionError.class) public void Z$020() {
    assert null != null;
  }

  /** This is the incorrect way you should check for nulls, using {@link azzert}
   * makes sure we get more informative messages */
  @Test public void Z$030() {
    azzert.isNull($null());
  }

  /** Correct way of checking for nulls. {@link azzert} cannot provide further
   * information if the test fails, since failures give null which carries no
   * information informative messages */
  @Test public void Z$040() {
    assert new Object() != null : "Weird... I (" + this + ") never knew that new can return null";
  }

  /** Correct way of checking for nulls. {@link azzert} cannot provide further
   * information if the test fails, since failures give null which carries no
   * information informative messages */
  @Test public void Z$050() {
    assert new Object() != null;
  }

  /** Correct way of checking for true value. {@link azzert} cannot provide
   * further information if the test fails, since failures give nothing but
   * boolean value. */
  @Test public void Z$060() {
    assert $true();
    assert $true() : "Failure in " + object();
  }

  /** Correct way of checking for false value. {@link azzert} cannot provide
   * further information if the test fails, since failures give nothing but
   * boolean value. */
  @Test public void Z$070() {
    assert !$false();
    assert !$false() : "Failure in " + object();
  }

  /** Correct way of checking types */
  @Test public void Z$080() {
    azzert.that(new ArrayList<>(), instanceOf(List.class));
  }

  /** Correct way of checking for inequality of values */
  @Test public void Z$090() {
    azzert.that(object(), not(object()));
  }

  /** Correct way of checking for equality of values */
  @Test public void Z$100() {
    azzert.that(sameSomeObject(), is(sameSomeObject()));
  }

  /** Correct way of checking for equality of numbers */
  @Test public void Z$110() {
    azzert.that($0(), is($0()));
    azzert.that($0(), not(is($1())));
  }

  /** Correct ways of comparing numbers */
  @Test public void Z$120() {
    azzert.that($0(), greaterThanOrEqualTo($0()));
    azzert.that($1(), greaterThanOrEqualTo($0()));
    azzert.that($1(), greaterThan($0()));
    azzert.that($0(), lessThanOrEqualTo($1()));
    azzert.that($0(), lessThanOrEqualTo($0()));
    azzert.that($0(), lessThan($1()));
  }

  /** Correct way of trimming does not change */
  @Test public void Z$130() {
    trimmingOf("a").stays();
  }

  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a").stays();
  }
}
