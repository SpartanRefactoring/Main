package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.junit.Test;

/** Tests {@link QuestionQuestion}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class QuestionQuestionTest {
  @Test public void basic() {
    trimmingOf("return hiChars == null ? 1 : hiChars.length;")//
        .using(new QuestionQuestion(), ConditionalExpression.class)//
        .gives("return take(hiChars.length).default¢(hiChars).to(1);")//
        .stays();
  }
}
