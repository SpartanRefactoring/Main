package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-01 */
@Ignore // TODO: Ori Marco
@SuppressWarnings("static-method")
public class ExhaustTest {
  @Test public void a() {
    trimmingOf("while (keyReferenceQueue.poll() != null) {}")//
        .using(WhileStatement.class, new Exhaust())//
        .gives("exhaust(()->keyReferenceQueue.poll());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("while (keyReferenceQueue.poll() != null) {something(); andAnother();}")//
        .using(WhileStatement.class, new Exhaust())//
        .stays();
  }
}
