package il.org.spartan.zoomer.zoomin.expanders;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.bloater.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Examples tests for bloaters.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-27 */
@RunWith(Parameterized.class)
public class BloatersExamplesTests extends ExamplesTests {
  public BloatersExamplesTests(Tipper<? extends ASTNode> tipper, String name) {
    super(tipper, name);
  }

  @Parameters(name = "{index}. {1}") //
  public static Collection<Object[]> data() {
    return allTippers().stream().map(λ -> new Object[] { λ, system.className(λ) }).collect(Collectors.toList());
  }

  /** Get all tippers from {@link Toolbox}. Removes duplicate tippers (same
   * class, different templates).
   * @return
   * @return all tippers to be tested */
  @SuppressWarnings("rawtypes") private static Collection<?> allTippers() {
    return InflaterProvider.freshCopyOfAllExpanders().getAllTippers() //
        .stream()
        .collect(Collectors.toMap((Function<Tipper<? extends ASTNode>, ? extends Class<? extends Tipper>>) Tipper<? extends ASTNode>::getClass,
            λ -> λ, (t1, t2) -> t1))
        .values();
  }
}
