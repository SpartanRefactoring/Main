package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Unit Test for the ForBlock expander {@link ForBlockExpander}, issue #975
 * Also, Unit Test for the WhileBlock expander {@link WhileBlockExpander}
 * @author Raviv Rachmiel
 * @since 26-12-16 */
@SuppressWarnings("static-method")
public class Issue0975 {
  @Test public void simpleBlockTest() {
    expansionOf("for(int i=0;i<5;i++) a=5;").gives("for(int i=0;i<5;i++){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTest() {
    expansionOf("for(int i=0;i<5;i++){ a=5;}").stays();
  }

  @Test public void notSimpleShouldntAddTest() {
    expansionOf("for(int i=0;i<5;i++){ a=5;b=3;}").stays();
  }

  @Test public void notSimpleShouldAddTest() {
    expansionOf("for(int i=0;i<5;i++) a=5; b=7;").gives("for(int i=0;i<5;i++){ a=5;}b=7;").stays();
  }

  @Ignore @Test public void simpleBlockTestWhile() {
    expansionOf("while(i<5) a=5;").gives("while(i<5){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTestWhile() {
    expansionOf("while(i<5){ a=5;}").stays();
  }

  @Test public void notSimpleShouldntAddTestWhile() {
    expansionOf("while(i<5){ a=5;b=3;}").stays();
  }

  @Ignore @Test public void notSimpleShouldAddTestWhile() {
    expansionOf("while(i<5) a=5; b=7;").gives("while(i<5){ a=5;}b=7;").stays();
  }
}
