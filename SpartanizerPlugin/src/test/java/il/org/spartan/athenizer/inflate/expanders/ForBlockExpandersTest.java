package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Test for the ForBlock expander, issue #975
 * @author Raviv Rachmiel
 * @since 26-12-16 */
@SuppressWarnings("static-method")
public class ForBlockExpandersTest {
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
}
