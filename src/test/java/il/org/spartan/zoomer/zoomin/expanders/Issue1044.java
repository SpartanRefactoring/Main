package il.org.spartan.zoomer.zoomin.expanders;

// import static org.junit.Assert.*;
import org.junit.*;

import il.org.spartan.spartanizer.java.namespace.*;

/** Test class for name generation from Namespace (Environments) 
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-10 */
@SuppressWarnings("static-method")
public class Issue1044 {
  @Ignore
  //TODO: Doron, this test fails
  @Test public void test() {
    (new Namespace(null, null)).generateName(new Binding());
  }
}
