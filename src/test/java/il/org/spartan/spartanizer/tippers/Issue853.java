package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue853 {
  @Ignore @Test public void test0() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;++y;}").withTipper(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from($L1).to(10)){{++x;++y;}}")
        .gives("for(Integer ¢ : range.from($L1).to(10)){++x;++y;}")
        .stays();
  }
}