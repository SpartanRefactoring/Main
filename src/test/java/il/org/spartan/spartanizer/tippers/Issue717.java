package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import static org.junit.Assert.*;

import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") public class Issue717 {

  @Test public void isCompiled() {
    assert true;
  }
  
  @Test  public void nullCheckReturnsFalse(){
    assertFalse (determineIf.hasBigBlock(null));
  }

}