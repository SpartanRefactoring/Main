package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Tests {@link DefaultsTo}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DefaultsToTest {
  @Test public void basic() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .gives("return default¢(¢).to(\"\");")//
        .stays();
  }
}
