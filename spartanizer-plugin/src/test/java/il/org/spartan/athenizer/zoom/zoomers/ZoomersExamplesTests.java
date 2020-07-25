package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fluent.ly.English;
import il.org.spartan.athenizer.InflaterProvider;
import il.org.spartan.spartanizer.tippers.ExamplesTests;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.Toolbox;
import il.org.spartan.utils.Example.Converts;
import il.org.spartan.utils.Example.Ignores;

/** Examples tests for bloaters.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-27 */
@RunWith(Parameterized.class)
public class ZoomersExamplesTests extends ExamplesTests {
  @Override protected void ignores(final Ignores ¢) {
    final Tipper<?>[] ¢1 = { tipper };
    wrap(() -> bloatingOf(¢.get()).using(¢1).stays());
  }
  @Override protected void converts(final Converts ¢) {
    final Tipper<?>[] ¢1 = { tipper };
    wrap(() -> bloatingOf(¢.from()).using(¢1).gives(¢.to()));
  }
  public ZoomersExamplesTests(final Tipper<? extends ASTNode> tipper, final String name) {
    super(tipper, name);
  }
  @Parameters(name = "{index}. {1}") //
  public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ, English.name(λ) }).collect(toList());
  }
  /** Get all tippers from {@link Toolbox}. Removes duplicate tippers (same
   * class, different templates).
   * @return all tippers to be tested */
  private static Collection<?> allTippers() {
    return InflaterProvider.freshCopyOfAllExpanders().getAllTippers() //
        .stream().collect(toMap(Tipper::getClass, λ -> λ, (t1, t2) -> t1)).values();
  }
}
