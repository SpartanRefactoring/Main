package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.utils.tdd.*;

/** Test class for tdd.enumerate.methods (see issue #679)
 * @author Sharon Kuninin & Yarden Lev
 * @since 2016-11-2 */
@SuppressWarnings("static-method") public class Issue679 {
  @Test public void checkExistence() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkReturnType() {
    @SuppressWarnings("unused") int $ = enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkParameterType() {
    enumerate.methods((CompilationUnit) null);
  }
}
