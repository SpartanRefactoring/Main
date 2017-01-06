package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1043 {
  @Test public void test() {
    trimmingOf("try (final File f = new File()){i++;}")  
    .gives("try (File f = new File()) {i++;}")  
        .stays();
  }
}
