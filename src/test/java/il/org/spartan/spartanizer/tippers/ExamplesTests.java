package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

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

/** A test generator for {@link Tipper}s, creating tests from their declared
 * examples (see {@link Tipper#examples}).
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-09 */
@RunWith(Parameterized.class)
@SuppressWarnings("static-method")
public class ExamplesTests {
  /** Current tipper, loaded dynamically. */
  private final Tipper<? extends ASTNode> tipper;

  /** Redirects examples to tests according to type */
  @Test public void testExamples() {
    for (final Example e : tipper.examples())
      try {
        if (e instanceof Converts)
          testConverts((Converts) e);
      } catch (final AssertionError x) {
        throw wrapFailure(x);
      }
  }

  private void testConverts(final Converts ¢) {
    trimmingOf(¢.from()).gives(¢.to());
  }

  public ExamplesTests(final Tipper<? extends ASTNode> tipper) {
    this.tipper = tipper;
  }

  private AssertionError wrapFailure(final AssertionError x) {
    return new AssertionError("Example failure at " + tipper.className() + ":\n" + x.getMessage().trim(), x.getCause());
  }

  @Parameters public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ }).collect(Collectors.toList());
  }

  /** Get all tippers from {@link Toolbox} and
   * {@link ADDITIONAL_TIPPERS_FOR_TESTING}
   * @return all tippers to be tested */
  private static Collection<Tipper<? extends ASTNode>> allTippers() {
    return Toolbox.freshCopyOfAllTippers().getAllTippers();
  }
}
