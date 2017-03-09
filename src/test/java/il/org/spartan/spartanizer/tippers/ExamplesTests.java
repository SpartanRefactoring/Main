package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;
import il.org.spartan.spartanizer.tipping.Tipper.Example.*;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

/** A test generator for {@link Tipper}s, creating tests from their declared
 * examples (see {@link Tipper#examples}).
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-09 */
@RunWith(Parameterized.class)
@SuppressWarnings("static-method")
public class ExamplesTests {
  // TODO: Ori Roth - kill this flag, I will explain on the phone why --yg
  /** In case we want to test tippers that are not in the {@link Toolbox}. */
  private static final Tipper<?>[] ADDITIONAL_TIPPERS_FOR_TESTING = {};
  /** Is true iff creates tests without examples (empty tests, always
   * successful). */
  // TODO: Ori Roth - kill this flag, I will explain on the phone why --yg
  private static final boolean TEST_WITHOUT_EXAMPLES = true;
  /** Current tipper, loaded dynamically. */
  private Tipper<? extends ASTNode> tipper;

  /** Redirects examples to tests according to type */
  @Test public void testExamples() {
    for (Example e : tipper.examples())
      try {
        if (e instanceof Converts)
          testConverts((Converts) e);
      } catch (AssertionError x) {
        throw wrapFailure(x);
      }
  }

  private void testConverts(Converts ¢) {
    trimmingOf(¢.from()).gives(¢.to());
  }

  public ExamplesTests(Tipper<? extends ASTNode> tipper) {
    this.tipper = tipper;
  }

  private AssertionError wrapFailure(AssertionError x) {
    return new AssertionError("Example failure at " + tipper.className() + ":\n" + x.getMessage().trim(), x.getCause());
  }

  @Parameters public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ }).collect(Collectors.toList());
  }

  /** Get all tippers from {@link Toolbox} and
   * {@link ADDITIONAL_TIPPERS_FOR_TESTING}
   * @return all tippers to be tested */
  private static Collection<Tipper<? extends ASTNode>> allTippers() {
    Set<Tipper<? extends ASTNode>> $ = new HashSet<>();
    $.addAll(Toolbox.freshCopyOfAllTippers().getAllTippers());
    Collections.addAll($, ADDITIONAL_TIPPERS_FOR_TESTING);
    if (!TEST_WITHOUT_EXAMPLES)
      $.removeIf(λ -> λ.examples().length == 0);
    return $;
  }
}
