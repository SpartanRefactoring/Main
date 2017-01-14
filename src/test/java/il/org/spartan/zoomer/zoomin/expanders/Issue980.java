package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test Class for Expand boolean expressions
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-13 */

@Ignore
@SuppressWarnings("static-method")
public class Issue980 {
  @Test public void test0() {
    zoomingInto("{return x && y();}")//
        .gives("boolean a = x; boolean b = y(); return a && b;");
  }

  @Test public void test1() {
    zoomingInto("return true && y;")//
        .gives("boolean a = true; boolean b = y(); return a&&b;");
  }

  @Test public void test2() {
    zoomingInto("boolean t =  x && y();")//
        .gives("boolean a = x; boolean b = y(); boolean t = a&&b;");
  }

  @Test public void test3() {
    zoomingInto("return x || y();")//
        .gives("boolean a = x; boolean b = y(); return a || b;");
  }

  @Test public void test4() {
    zoomingInto("return true || y;")//
        .gives("boolean a = true; boolean b = y(); return a || b;");
  }

  @Test public void test5() {
    zoomingInto("boolean t =  x || y();")//
        .gives("boolean a = x; boolean b = y(); boolean t = a || b;");
  }

  @Test public void test6() {
    zoomingInto("return x && y() || z;")//
        .gives("boolean a = x && y();return a && z;")//
        .gives("boolean a = x;a = a && y();return a && z;").stays();
  }
}
