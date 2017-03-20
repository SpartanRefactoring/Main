package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Example.*;

/** A test generator for {@link Tipper}s, creating tests from their declared
 * examples (see {@link Tipper#examples}).
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-09 */
@RunWith(Parameterized.class)
public class ExamplesTests {
  /** Current tipper, loaded dynamically. */
  private final Tipper<? extends ASTNode> tipper;

  /** Redirects examples to tests according to type */
  @Test public void converts() {
    Arrays.stream(tipper.examples()).filter(Converts.class::isInstance).forEachOrdered(λ -> testConverts((Converts) λ));
  }

  /** Redirects examples to tests according to type */
  @Test public void ignores() {
    Arrays.stream(tipper.examples()).filter(Ignores.class::isInstance).forEachOrdered(λ -> ignores((Ignores) λ));
  }

  private static void ignores(final Ignores ¢) {
    trimmingOf(¢.get()).stays();
  }

  private static void testConverts(final Converts ¢) {
    trimmingOf(¢.from()).gives(¢.to());
  }

  public ExamplesTests(final Tipper<? extends ASTNode> tipper, @SuppressWarnings("unused") final String name) {
    this.tipper = tipper;
  }

  @Parameters(name = "{index}. {1}") //
  public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ, system.className(λ) }).collect(Collectors.toList());
  }

  /** Get all tippers from {@link Toolbox} and
   * {@link ADDITIONAL_TIPPERS_FOR_TESTING}
   * @return all tippers to be tested */
  private static Collection<Tipper<? extends ASTNode>> allTippers() {
    return Toolbox.freshCopyOfAllTippers().getAllTippers();
  }
}
