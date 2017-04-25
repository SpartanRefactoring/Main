package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import java.util.*;

import org.junit.*;
import il.org.spartan.*;

/** A template for unit tests for {@link NameYourClassHere} , can be used by:
 * <ul>
 * <li>Create a new test class that {@code extends} this class.
 * <ol>
 * <li>All methods in this class will be executed in your code.
 * <li>You would probably want to copy the {@code imports} of this class.
 * </ol>
 * <li>Create a <u>new</u> test class by copying the source of this one:
 * <ol>
 * <li>edit this javaDoc in your copy.
 * <li>put your name in the header
 * <li>sooner or later you will want to erase from your copy:
 * <ul>
 * <li>{@link Test} copied from this class
 * <li>auxiliary {@code private} methods
 * <li>everything else copied from this class
 * </ul>
 * </ol>
 * In writing tests, note the following
 * <ol>
 * <li>Use {@code assert} statement for the following:
 * <ul>
 * <li>{@code assert</b> something();</code>
 * <li>{@code assert</b> !something();</code>
 * <li>{@code assert</b> thing() != null;</code>
 *
</ul>
 * The reason is that {@code assert} is short, clear, but does not provide as
 * much information as {@link azzert} . In the above situations, a priori there
 * is nothing to print.
 * <p>
 * You can augment {@code assert</b> with a short {@link Object} (usually a
 * {@link String} ) that can provide some debugging information (see the
 * implementation of {@link #Z$040()} or {@link #Z$060()} .)
 * <li>Instead, use class {@link azzert} for anything else:
 * <ul>
 * <li>to check whether something is {@code null, use
 * {@code azzer.notNull(something)} which prints the content of this something
 * if it is not {@code null</b></code<li>Test methods do not usually contain
 * the word test in them. There is no point in repeating ourselves.
 * <li>Naming convention here is that test methods begin with a capital letter
 * A...Z, and are numbered.
 * <li>Also, as in BASIC, tests numbering is in multiple of tens, so as to make
 * it possible to insert tests between tests.
 * <li>Using series for tests is the best way for doing TDD (test driven
 * development)
 * </ol>
 * @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue____ {
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
    azzert.that(an.empty.list(), instanceOf(List.class));
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
    trimmingOf("a")//
        .stays();
  }

  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a")//
        .stays();
  }
}
