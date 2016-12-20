package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class GenerlizedSwitchTernaryTest {
  @Test public void basic() {
    trimmingOf("d = x == 0 ? d1() : x == 1 ? d2() : d3();")//
        .withTipper(ConditionalExpression.class, new GeneralizedSwitchTernary())//
        .gives("d = holds(¢->x==¢).on(0,__->d1()).on(1,__->d2()).elze(__->d3());")//
        .stays();
  }
}
