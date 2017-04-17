package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link QuestionQuestion}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class TakeDefaultToTest {
  @Test public void basic() {
    trimminKof("return hiChars == null ? 1 : hiChars.length;")//
        .using(new QuestionQuestion(), ConditionalExpression.class)//
        .gives("return take(hiChars.length).default¢(hiChars).to(1);")//
        .stays();
  }
}
