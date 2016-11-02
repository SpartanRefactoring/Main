package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/**
 * see issue #714 for more details
 * @author Dan Abramovich
 * @author Assaf Lustig
 * @author Arthur Sapozhnikov
 * @since 16-11-02
 */

public class Issue714 {
  
  @SuppressWarnings("static-method") @Test public void testRetTypeCompiles() {
    @SuppressWarnings("unused") final boolean b = determineIf.isImmutable(null);
  }
  
  @Test public void testNull(){
    auxBool(determineIf.isImmutable((TypeDeclaration) null));
  }
  
  
  static void auxBool(@SuppressWarnings("unused") final boolean __) {
    assert true;
  }
}
