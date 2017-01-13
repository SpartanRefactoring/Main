package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dan Abramovich
 * @since 28-11-2016 */
@Ignore // TODO: Raviv Rachmiel --yg
@SuppressWarnings("static-method")
public class Issue0853 {
  @Test public void test0() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;}").using(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(3).to(10)){{++x}}").gives("for(Integer ¢ : range.from($L1).to(10)){++x;++y;}").stays();
  }

  @Test public void test1() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;¢+=x;++y;}").using(ForStatement.class, new ReplaceForWithRange()).stays();
  }

  @Test public void test2() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;++¢;++y;}").using(ForStatement.class, new ReplaceForWithRange()).stays();
  }

  @Test public void test3() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; --¢){++x;++y;}").using(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(3).step(-1).to(5)){{++x;++y;}}").gives("for(Integer ¢ : range.from($L1).step(-1).to(5)){++x;++y;}")
        .stays();
  }

  @Test public void test4() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; ¢-=2){++x;++y;}").using(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(10).step(-2).to(5)){{++x;++y;}}").gives("for(Integer ¢ : range.from($L1).step(-2).to(5)){++x;++y;}")
        .stays();
  }
}
