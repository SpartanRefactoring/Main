package il.org.spartan.spartanizer.tippers;


import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Raviv Rachmiel
 * @since 25-11-2016 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue305 {
  
  @Test public void forTestNoChange() {
    trimmingOf("for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()));").stays();
    assert true;
  }
  

  
  
  @Test public void forTestChangeBasic() {
    trimmingOf("for(int ¢=0;¢;++¢); return true;").gives("for(int ¢=0;;++¢) return true;").stays();
    assert true;
  }
  

}
