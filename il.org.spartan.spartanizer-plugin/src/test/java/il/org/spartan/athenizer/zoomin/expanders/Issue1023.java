package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit Test for the ForEachBlock expander {@link ForEachBlockBloater}
 * @author Raviv Rachmiel
 * @since 11-1-17 */
@SuppressWarnings("static-method")
public class Issue1023 {
  @Test public void notSimpleShouldAddTest() {
    bloatingOf("for(Double i : lili) a=5; b=7;")//
        .gives("for(Double i : lili){a=5;}b=7;");
  }
  @Test public void notSimpleShouldntAddTest() {
    bloatingOf("for(Double i : lili){ a=5;b=3;}")//
        .stays();
  }
  @Test public void simpleBlockTest() {
    bloatingOf("for(int i : lili) a=5;")//
        .gives("for(int i : lili){a=5;}")//
        .stays();
  }
  @Test public void simpleShouldntAddTest() {
    bloatingOf("for(int i : lili){ a=5;}")//
        .stays();
  }
}
