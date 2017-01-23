package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO:  orimarco <tt>marcovitch.ori@gmail.com</tt>
 please add a description 
 @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 
 */

@SuppressWarnings("static-method")
public class TakeDefaultToTest {
  @Test public void basic() {
    trimmingOf("return hiChars == null ? 1 : hiChars.length;")//
        .using(ConditionalExpression.class, new TakeDefaultTo())//
        .gives("return take(hiChars.length).default¢(hiChars).to(1);")//
        .stays();
  }
}

