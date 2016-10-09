package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method") public class Issue378 {
  @Test public void a() {
    trimmingOf("void func(int i) {return something_else;}").gives("void func(int __) {return something_else;}").stays();
  }

  @Ignore @Test public void b() {
    trimmingOf("void func(int notJD) {return something_else;}").stays();
  }
}
