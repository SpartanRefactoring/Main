package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: Dan Abramovich please add a description
 * @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue0894 {
  // return $L1
  @Test public void test0() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;}") //
        .using(ForStatement.class, new ReplaceForWithRange()).gives("for(Integer ¢ : range.from(3).to(10)){{++x;}}")//
        .gives("for(Integer ¢ : range.from(3).to(10))++x;")//
        .stays();
  }

  // ¢-=2 matches ++$N for some reason
  @Test public void test2() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; ¢-=2){++x;++y;}") //
        .using(ForStatement.class, new ReplaceForWithRange()).gives("for(Integer ¢ : range.from(10).step(-2).to(5)){{++x;++y;}}")//
        .gives("for(Integer ¢ : range.from(10).step(-2).to(5)){++x;++y;}")//
        .stays();
  }
}
