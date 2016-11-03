package il.org.spartan.spartanizer.utils.tdd;

import org.junit.*;

/** @author Shimon Azulay & Idan Atias & Lior Ben Ami
 * @since 16-11-3 */

@SuppressWarnings({ "static-method", "javadoc" }) 
public class Issue741 {
  
  @Test public void publicFields_test0() {
    getAll2.publicFields(null);
    assert true;
  } 
}
