package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class TakeDefaultToTest {
  @Test public void basic() {
    trimmingOf("return hiChars == null ? 1 : hiChars.length;")//
        .withTipper(ConditionalExpression.class, new TakeDefaultTo())//
        .gives("return take(hiChars.length).default¢(hiChars).to(1);")//
        .stays();
  }
}
