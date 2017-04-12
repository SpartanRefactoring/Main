package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import static java.util.stream.Collectors.*;

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
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-03-09 */
@RunWith(Parameterized.class)
public class ExamplesTests {
  /** Current tipper, loaded dynamically. */
  protected final Tipper<? extends ASTNode> tipper;

  /** Redirects examples to tests according to type */
  @Test public void converts() {
    Stream.of(tipper.examples()).filter(Converts.class::isInstance).map(Converts.class::cast).forEachOrdered(this::converts);
  }

  /** Redirects examples to tests according to type */
  @Test public void ignores() {
    Stream.of(tipper.examples()).filter(Ignores.class::isInstance).map(Ignores.class::cast).forEachOrdered(this::ignores);
  }

  protected void ignores(final Ignores ¢) {
    wrap(() -> trimminKof(¢.get()).usingTipper(tipper).stays());
  }

  protected void converts(final Converts ¢) {
    wrap(() -> trimminKof(¢.from()).usingTipper(tipper).gives(¢.to()));
  }

  protected void wrap(final Runnable test) {
    try {
      test.run();
    } catch (final AssertionError x) {
      throw new AssertionError("Example failure at " + tipper.tipperName() + ":\n" + x.getMessage().trim(), x.getCause());
    }
  }

  public ExamplesTests(final Tipper<? extends ASTNode> tipper, @SuppressWarnings("unused") final String name) {
    this.tipper = tipper;
  }

  @Parameters(name = "{index}. {1}") //
  public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ, English.name(λ) }).collect(toList());
  }

  /** Get all tippers from {@link Configuration}. Removes duplicate tippers
   * (same class, different templates).
   * @return
   * @return all tippers to be tested */
  private static Collection<?> allTippers() {
    return Configurations.allClone().getAllTippers() //
        .stream().collect(toMap(Tipper::getClass, λ -> λ, (t1, t2) -> t1)).values();
  }
}
