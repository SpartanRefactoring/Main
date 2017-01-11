package il.org.spartan.zoomer.zoomin.expanders;
import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit Test for the ForBlock expander {@link ForEachBlockExpander}
 * @author Raviv Rachmiel
 * @since 11-1-17 */
@SuppressWarnings("static-method")
public class Issue1023 {
  @Test public void simpleBlockTest() {
    zoomingInto("for(int i=0;i<5;i++) a=5;").gives("for(int i=0;i<5;i++){a=5;}")
    .gives("for(int i=0;i<5;i=i+1){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTest() {
    zoomingInto("for(int i=0;i<5;i++){ a=5;}")//
    .gives("for(int i=0;i<5;i=i+1){a=5;}")//
    .stays();
  }

  @Test public void notSimpleShouldntAddTest() {
    zoomingInto("for(int i=0;i<5;i++){ a=5;b=3;}")//
    .gives("for(int i=0;i<5;i=i+1){a=5;b=3;}")//
    .stays();
  }

  @Test public void notSimpleShouldAddTest() {
    zoomingInto("for(int i=0;i<5;i++) a=5; b=7;").gives("for(int i=0;i<5;i++){a=5;}b=7;");
  }

  @Ignore @Test public void simpleBlockTestWhile() {
    zoomingInto("while(i<5) a=5;").gives("while(i<5){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTestWhile() {
    zoomingInto("while(i<5){ a=5;}").stays();
  }

  @Test public void notSimpleShouldntAddTestWhile() {
    zoomingInto("while(i<5){ a=5;b=3;}").stays();
  }

  @Ignore @Test public void notSimpleShouldAddTestWhile() {
    zoomingInto("while(i<5) a=5; b=7;").gives("while(i<5){ a=5;}b=7;").stays();
  }
}
