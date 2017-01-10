package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit Test for the ForBlock expander {@link ForBlockExpander}, issue #975
 * Also, Unit Test for the WhileBlock expander {@link WhileBlockExpander}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
@SuppressWarnings("static-method")
public class Issue0975 {
  @Test public void simpleBlockTest() {
    zoomingInto("for(int i : lili) a=5;").gives("for(int i : lili){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTest() {
    zoomingInto("for(int i : lili){ a=5;}")//
    .stays();
  }

  @Test public void notSimpleShouldntAddTest() {
    zoomingInto("for(Double i : lili){ a=5;b=3;}")//
    .stays();
  }

  @Test public void notSimpleShouldAddTest() {
    zoomingInto("for(Double i : lili) a=5; b=7;").gives("for(Double i : lili){a=5;}b=7;");
  }

}

