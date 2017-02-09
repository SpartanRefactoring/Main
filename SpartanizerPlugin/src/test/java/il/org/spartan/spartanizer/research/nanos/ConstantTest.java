package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Constant}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-09 */
@SuppressWarnings("static-method")
public class ConstantTest {
  @Test public void basic() {
    trimmingOf("class C{public static final int a = 7;}")//
        .using(FieldDeclaration.class, new Constant())//
        .gives("")//
        .stays();
  }
}
