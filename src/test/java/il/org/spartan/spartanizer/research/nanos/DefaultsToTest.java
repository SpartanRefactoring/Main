package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class DefaultsToTest {
  @Test public void basic() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .gives("return default¢(¢).to(\"\");")//
        .stays();
  }
}
