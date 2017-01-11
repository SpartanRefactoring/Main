package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test case for @link MultiplicationToDoubleCast}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-01-11 */
@SuppressWarnings("static-method")
public class Issue1007 {
  @Test public void t1() {
    zoomingInto("2 * 1.0").gives("(double) 2");
  }

  @Test public void t2() {
    zoomingInto("1.0 * a").gives("(double) a");
  }

  @Test public void t3() {
    zoomingInto("1. * a").gives("(double) a");
  }

  @Test public void t4() {
    zoomingInto("a * 1.").gives("(double) a");
  }

  @Test public void t5() {
    zoomingInto("1. * 9 * a").gives("(double) 9 * a");
  }

  @Test public void t6() {
    zoomingInto("1. * 9 * a * b").gives("(double) 9 * a * b");
  }

  @Test public void t7() {
    zoomingInto("9 * a * 1. * b").gives("(double) 9 * a * b");
  }
}
