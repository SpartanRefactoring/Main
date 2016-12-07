package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class IfNullReturnNullTest {
  @Test public void a() {
    trimmingOf("if(x == null) return null; use();").withTipper(IfStatement.class, new IfNullReturnNull()).gives("If.Null(x).returnsNull(); use();");
  }

  @Test public void b() {
    trimmingOf("if(x.y(wiz,this,and(zis)) == null) return null;").withTipper(IfStatement.class, new IfNullReturnNull())
        .gives("If.Null(x.y(wiz,this,and(zis))).returnsNull();");
  }
}
