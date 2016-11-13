package il.org.spartan.spartanizer.cmdline;

import org.junit.*;

/** @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 12, 2016 */
@SuppressWarnings("static-method") public class codeTest {
  @Test public void noWords() {
    Assert.assertEquals(0, code.wc(""));
  }
  @Test public void oneWord() {
    Assert.assertEquals(1, code.wc("hello"));
  }
  @Test public void fiveWords() {
    Assert.assertEquals(5, code.wc("hello world how are you?"));
  }
  @Test public void zeroIsReturnedForNull() {
    Assert.assertEquals(0, code.wc(null));
  }
  @Test public void twoSpaces() {
    Assert.assertEquals("a a", code.essence("a  a"));
  }
  @Test public void emptyLine() {
    Assert.assertEquals("", code.essence("        \n"));
  }
  @Test public void spaceIsRemovedBetweenParenthesis() {
    Assert.assertEquals("(2)(3)", code.essence("(2) (3)"));
  }
}
