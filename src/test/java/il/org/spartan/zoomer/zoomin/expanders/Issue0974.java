package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test class for issue #974
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0974 {
  @Test public void test0() {
    expansionOf("i++;").gives("i = i+1;").stays();
  }

  @Test public void test1() {
    expansionOf("i--;").gives("i = i-1;").stays();
  }

  @Test public void test2() {
    expansionOf("++i;").gives("i++;").gives("i = i+1;").stays();
  }

  @Test public void test3() {
    expansionOf("--i;").gives("i--;").gives("i = i-1;").stays();
  }

  @Test public void test4() {
    expansionOf("x = f(i--);").stays();
  }

  @Test public void test5() {
    expansionOf("x = f(i++);").stays();
  }

  @Test public void test6() {
    expansionOf("x = f(i--);").stays();
  }
}
