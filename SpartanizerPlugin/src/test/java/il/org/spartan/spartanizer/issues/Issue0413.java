package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;

import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Tests {@link Essence#stringRemove}
 * <p>
 * This class has more JavaDOC then appropriate for test classes. This is to
 * demonstrate and explain how TDD is carried out.
 * <p>
 * Note that:
 * <ol>
 * <li>Fluent API class {@link azzert} is used for testing.
 * <li>{@code static} members of fluent API class {@link azzert} are used for
 * fluent coding
 * <ul>
 * <li>azzert.that(Essence.stringRemove("abc"), iz("abc"));
 * <li>azzert.that(Essence.stringRemove("abc"), instanceOf(String.class));
 * </ul>
 * @author Yossi Gil
 * @since 2016 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0413 {
  /** There are no established rules on names of test methods. Here are some
   * recommendations:
   * <ul>
   * <li><b>DRY</b>: Don't Repeat Yourself; if a method is annotated with a
   * {@code @Test}, it is a matter of DRY to include the word `test` in the
   * method's name
   * <li><b>Flavors</b>: name a test that prove that something is not done,
   * {@code chocolate()}. If there are several, by names {@code chocolate01},
   * code>chocolate02</code>, etc
   * </ul>
  */
  @Test public void chocolate0() {
    azzert.that(Essence.stringRemove("abc"), iz("abc"));
  }

  @Test public void chocolate1() {
    azzert.that(Essence.stringRemove("abc"), instanceOf(String.class));
  }

  @Test public void chocolate2() {
    azzert.that(Essence.stringRemove(Essence.stringRemove("hello")), instanceOf(String.class));
  }

  @Test public void idempotent() {
    as.list(new String[] { "This", "This 'is'", "This \"is" }).forEach(λ -> azzert.that(Essence.stringRemove(Essence.stringRemove(λ)), iz(λ)));
  }

  /** Where all not-yet working tests are.
   * <p/>
   * On the long-run, this class should loose all its members, but still remain
   * as a place holder for bugs and faults that haven't been discovered by tests
   * of the tests in the containing class */
  @Ignore("Placeholder: dont remove") //
  static class WorkInProgress {
    /** We only need this field, to keep Eclipse happy about the class not being
     * empty. */
    /** Flavor sour is for a bit tricky case */
    @Test public void sour() {
      azzert.that(
          Essence.stringRemove(//
              "\"string literal with an \\\"embedded\\\" string literal\""), //
          iz(""));
    }

    /** Flavor vanilla is for the simplest cases */
    @Test public void vanilla01() {
      azzert.that(Essence.stringRemove("\"Who\" is on \"First\""), //
          iz("is on"));
    }

    /** Flavor vanilla is for the simplest cases */
    @Test public void vanilla02() {
      azzert.that(Essence.stringRemove("\"Who\" is on \"First\""), //
          is("is on"));
    }
  }
}
