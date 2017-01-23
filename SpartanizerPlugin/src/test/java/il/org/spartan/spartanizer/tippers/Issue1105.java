package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests of {@link LambdaExpressionCentification}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt> */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1105 {
  @Test public void a() {
    trimmingOf("(a)->a").gives("(¢)->¢").stays();
  }
}
