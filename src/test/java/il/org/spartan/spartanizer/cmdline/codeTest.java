package il.org.spartan.spartanizer.cmdline;

import org.junit.*;

/** @author Sharon Kuninin
 * @author Yarden Lev
 * @since Nov 12, 2016 */
@SuppressWarnings("static-method") public class codeTest {
  @Test public void noWordsTest() {
    Assert.assertEquals(0, code.wc(""));
  }
}
