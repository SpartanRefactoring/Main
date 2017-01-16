package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit Test for the ForBlock expander {@link ForEachBlockExpander}
 * @author Raviv Rachmiel
 * @since 11-1-17 */
@SuppressWarnings("static-method")
public class Issue1023 {
  @Ignore
  static class ToFix { // should pass after fixing {@link Issue0974}
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

    @Test public void notSimpleShouldntAddTest() {
      bloatingOf("for(int i=0;i<5;i++){ a=5;b=3;}")//
          .gives("for(int i=0;i<5;i=i+1){a=5;b=3;}")//
          .stays();
    }
  }

  @Test public void notSimpleShouldAddTest() {
    bloatingOf("for(int i=0;i<5;i++) a=5; b=7;")//
        .gives("for(int i=0;i<5;i++){a=5;}b=7;");
  }

  @Ignore @Test public void simpleBlockTestWhile() {
    bloatingOf("while(i<5) a=5;")//
        .gives("while(i<5){a=5;}")//
        .stays();
  }

  @Test public void simpleShouldntAddTestWhile() {
    bloatingOf("while(i<5){ a=5;}")//
        .stays();
  }

  @Test public void notSimpleShouldntAddTestWhile() {
    bloatingOf("while(i<5){ a=5;b=3;}")//
        .stays();
  }

  @Ignore @Test public void notSimpleShouldAddTestWhile() {
    bloatingOf("while(i<5) a=5; b=7;")//
        .gives("while(i<5){ a=5;}b=7;")//
        .stays();
  }
}
