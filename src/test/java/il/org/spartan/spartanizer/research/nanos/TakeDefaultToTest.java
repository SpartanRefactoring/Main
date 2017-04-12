package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.research.nanos.*;

/** Tests {@link TakeDefaultTo}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class TakeDefaultToTest {
  @Test public void basic() {
    trimminKof("return hiChars == null ? 1 : hiChars.length;")//
        .using(new TakeDefaultTo(), ConditionalExpression.class)//
        .gives("return take(hiChars.length).default¢(hiChars).to(1);")//
        .stays();
  }
}
