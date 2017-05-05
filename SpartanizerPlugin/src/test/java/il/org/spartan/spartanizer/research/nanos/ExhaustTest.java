package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/* Tests {@link Exhaust}
 *
 * @author orimarco {@code marcovitch.ori@gmail.com}
 *
 * @since 2017-01-01 */
@SuppressWarnings("static-method")
public class ExhaustTest {
  @Test public void a() {
    trimmingOf("while (keyReferenceQueue.poll() != null) {}")//
        .using(new Exhaust(), WhileStatement.class)//
        .gives("exhaust(()->keyReferenceQueue.poll()!=null);")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("while (keyReferenceQueue.poll() != null) {something(); andAnother();}")//
        .using(new Exhaust(), WhileStatement.class)//
        .stays();
  }
}
