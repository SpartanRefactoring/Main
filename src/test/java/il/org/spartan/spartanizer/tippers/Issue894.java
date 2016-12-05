package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue894 {
  //return $L1
  @Test public void test0() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;}").withTipper(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(3).to(10)){{++x}}").gives("for(Integer ¢ : range.from($L1).to(10)){++x;++y;}").stays();
  }
  //does nothing on empty block (maybe it suppose to be like this?)
  @Test public void test1() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){}").withTipper(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(3).to(10)){}").stays();
  }
  // ¢-=2 matches ++$N for some reason
  @Test public void test2() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; ¢-=2){++x;++y;}").withTipper(ForStatement.class, new ReplaceForWithRange())
        .gives("for(Integer ¢ : range.from(10).step(-2).to(5)){{++x;++y;}}").gives("for(Integer ¢ : range.from($L1).step(-2).to(5)){++x;++y;}")
        .stays();
  }
}