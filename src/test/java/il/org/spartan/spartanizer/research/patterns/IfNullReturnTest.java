package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class IfNullReturnTest {
  @Test public void a() {
    trimmingOf("if(x == null) return; use(); use();").using(IfStatement.class, new IfNullReturn()).gives("precondition.notNull(x); use(); use();");
  }
}
