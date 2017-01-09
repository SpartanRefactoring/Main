package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test class for issue #974
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class Issue0974 {
  @Test public void test0() {
    zoomingInto("i++;").gives("i = i+1;").stays();
  }

  @Test public void test1() {
    zoomingInto("i--;").gives("i = i-1;").stays();
  }

  @Test public void test2() {
    zoomingInto("++i;").gives("i++;").gives("i = i+1;").stays();
  }

  @Test public void test3() {
    zoomingInto("--i;").gives("i--;").gives("i = i-1;").stays();
  }

  @Test public void test4() {
    zoomingInto("x = f(i--);").gives("x = f(i=i-1);").stays();
  }

  @Test public void test5() {
    zoomingInto("x = f(i++);").gives("x = f(i=i+1);").stays();
  }
}
