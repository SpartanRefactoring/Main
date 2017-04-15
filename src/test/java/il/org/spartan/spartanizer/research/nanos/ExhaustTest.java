package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.research.nanos.deprecated.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/* Tests {@link Exhaust}
 *
 * @author orimarco {@code marcovitch.ori@gmail.com}
 *
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class ExhaustTest {
  @Test public void a() {
    trimminKof("while (keyReferenceQueue.poll() != null) {}")//
        .using(new Exhaust(), WhileStatement.class)//
        .gives("exhaust(()->keyReferenceQueue.poll()!=null);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("while (keyReferenceQueue.poll() != null) {something(); andAnother();}")//
        .using(new Exhaust(), WhileStatement.class)//
        .stays();
  }
}
