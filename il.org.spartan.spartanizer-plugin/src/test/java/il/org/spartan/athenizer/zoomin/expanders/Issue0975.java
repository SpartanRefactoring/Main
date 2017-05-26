package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit Test for the ForBlock expander {@link ForBlockBloater} Also, Unit Test
 * for the WhileBlock expander {@link WhileBlockBloater}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
@SuppressWarnings("static-method")
public class Issue0975 {
  @Test public void notSimpleShouldAddTest() {
    bloatingOf("for(int i=0;i<5;i++) a=5; b=7;")//
        .gives("for(int i=0;i<5;i++){a=5;}b=7;");
  }
  @Test public void notSimpleShouldAddTestWhile() {
    bloatingOf("while(i<5) a=5; b=7;")//
        .gives("while(i<5){ a=5;}b=7;")//
        .stays();
  }
  @Test public void notSimpleShouldntAddTestWhile() {
    bloatingOf("while(i<5){ a=5;b=3;}")//
        .stays();
  }
  @Test public void simpleBlockTestWhile() {
    bloatingOf("while(i<5) a=5;")//
        .gives("while(i<5){a=5;}")//
        .stays();
  }
  @Test public void simpleShouldntAddTestWhile() {
    bloatingOf("while(i<5){ a=5;}")//
        .stays();
  }

  static class ToFix { // should pass after fixing {@link Issue0974}
    @Test public void notSimpleShouldntAddTest() {
      bloatingOf("for(int i=0;i<5;i++){ a=5;b=3;}")//
          .gives("for(int i=0;i<5;i=i+1){a=5;b=3;}")//
          .stays();
    }
    @Test public void simpleBlockTest() {
      bloatingOf("for(int i=0;i<5;i++) a=5;")//
          .gives("for(int i=0;i<5;i++){a=5;}")//
          .gives("for(int i=0;i<5;i=i+1){a=5;}")//
          .stays();
    }
    @Test public void simpleShouldntAddTest() {
      bloatingOf("for(int i=0;i<5;i++){ a=5;}")//
          .gives("for(int i=0;i<5;i=i+1){a=5;}")//
          .stays();
    }
  }
}
