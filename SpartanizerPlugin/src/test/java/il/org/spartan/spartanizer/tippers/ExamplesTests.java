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

/** TODO Ori Roth: document class {@link }
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
@RunWith(Parameterized.class)
public class ExamplesTests {
  // In case we want to test tippers that are not in the {@link Toolbox}.
  private static final Tipper<?>[] ADDITIONAL_TIPPERS_FOR_TESTING = {};
  private static final boolean TEST_WITHOUT_EXAMPLES = true;
  private Tipper<? extends ASTNode> tipper;

  @Test public void testExamples() {
    for (Example e : tipper.examples())
      try {
        if (e instanceof Converts)
          testConverts((Converts) e);
      } catch (AssertionError x) {
        throw wrapFailure(x);
      }
  }

  private void testConverts(Converts c) {
    trimmingOf(c.from()).gives(c.to());
  }

  public ExamplesTests(Tipper<? extends ASTNode> tipper) {
    this.tipper = tipper;
  }

  private AssertionError wrapFailure(AssertionError x) {
    return new AssertionError("Example failure at " + tipper.className() + ":" + x.getMessage(), x.getCause());
  }

  @Parameters public static Collection<Object[]> data() {
    return allTippers().stream().map(t -> new Object[] { t }).collect(Collectors.toList());
  }

  private static Collection<Tipper<? extends ASTNode>> allTippers() {
    Set<Tipper<? extends ASTNode>> $ = new HashSet<>();
    $.addAll(Toolbox.freshCopyOfAllTippers().getAllTippers());
    Collections.addAll($, ADDITIONAL_TIPPERS_FOR_TESTING);
    if (!TEST_WITHOUT_EXAMPLES)
      $.removeIf(t -> t.examples().length == 0);
    return $;
  }
}
