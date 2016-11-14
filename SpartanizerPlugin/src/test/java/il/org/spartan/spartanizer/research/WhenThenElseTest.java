package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class WhenThenElseTest {
  @Test public void basic() {
    trimmingOf("return ¢ == null ? null : x.apply(¢) + \"\";").withTipper(ConditionalExpression.class, new WhenThenElse())
        .gives("return when(¢ == null).then(null).elze(x.apply(¢) + \"\");");
  }
}
