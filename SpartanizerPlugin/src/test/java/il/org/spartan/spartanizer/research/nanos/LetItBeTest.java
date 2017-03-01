package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link LetItBeIn}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-01 */
@SuppressWarnings("static-method")
public class LetItBeTest {
  @Test public void a() {
    trimmingOf("{{A x = foo(); return bar(x,x);} another();}")//
        .using(VariableDeclarationFragment.class, new LetItBeIn())//
        .gives("{{ return bar(foo(),foo());} another();}")//
        .stays();
  }
}
