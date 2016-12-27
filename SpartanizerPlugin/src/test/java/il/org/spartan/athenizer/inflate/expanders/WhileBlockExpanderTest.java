package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Test for the WhileBlock expander, issue #975
 * @author Raviv Rachmiel
 * @since 26-12-16 */

@Ignore
@SuppressWarnings("static-method")
public class WhileBlockExpanderTest {
  @Test public void simpleBlockTest() {
    expansionOf("while(i<5) a=5;").gives("while(i<5){a=5;}").stays();
  }

  @Test public void simpleShouldntAddTest() {
    expansionOf("while(i<5){ a=5;}").stays();
  }
  
  @Test public void notSimpleShouldntAddTest() {
    expansionOf("while(i<5){ a=5;b=3;}").stays();
  }

  @Test public void notSimpleShouldAddTest() {
    expansionOf("while(i<5) a=5; b=7;").gives("while(i<5){ a=5;}b=7;").stays();
  }
}
