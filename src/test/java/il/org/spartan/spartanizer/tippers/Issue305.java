package il.org.spartan.spartanizer.tippers;


import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Raviv Rachmiel
 * @since 25-11-2016 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue305 {
  
  @Test
  public void testCanTip() {
    assert ((new ForAndReturnToFor()).canTip((ForStatement) into.s("for (int i=0;i<5;i++) ; return false;")));
  }
  
  @Ignore
  @Test public void forTest0() {
    trimmingOf("for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()))").gives("for (String line = r.readLine(); line != null; $.append(line).append(System.lineSeparator()))").stays();
    assert true;
  }
  
  @Ignore
  @Test public void forTest1() {
    trimmingOf("for (int ¢=0;¢<5;++¢) ; return false;").gives("for (int ¢=0;$.append(line).append(System.lineSeparator()));++¢) ; return false;").stays();
    assert true;
  }

}
