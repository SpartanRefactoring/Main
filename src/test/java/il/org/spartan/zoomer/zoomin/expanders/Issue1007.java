package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test case for @link MultiplicationToDoubleCast}
 * covers also Issue #1006
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-01-11 */
@SuppressWarnings("static-method")
public class Issue1007 {
  @Test public void t1() {
    zoomingInto("double  t; t = 2 * 1.0;").gives("double t; t = (double) 2;");
  }

  @Test public void t2() {
    zoomingInto("double  t; t = 1.0 * a;").gives("double  t; t =(double) a;");
  }

  @Test public void t3() {
    zoomingInto("double  t; t =1. * a;").gives("double  t; t =(double) a;");
  }

  @Test public void t4() {
    zoomingInto("double  t; t =a * 1.;").gives("double  t; t =(double) a;");
  }

  @Test public void t5() {
    zoomingInto("double  t; t =1. * 9 * a;").gives("double  t; t =(double) 9 * a;");
  }

  @Test public void t6() {
    zoomingInto("double  t; t =1. * 9 * a * b;").gives("double  t; t =(double) 9 * a * b;");
  }

  @Test public void t7() {
    zoomingInto("double  t; t =9 * a * 1. * b;").gives("double  t; t =(double) 9 * a * b;");
  }

  @Test public void t8() {
    zoomingInto("double  t; t = 2 * 1L;").gives("double t; t = (long) 2;");
  }

  @Test public void t9() {
    zoomingInto("double  t; t = 1L * a;").gives("double  t; t =(long) a;");
  }

  @Test public void t10() {
    zoomingInto("double  t; t =1L * a;").gives("double  t; t =(long) a;");
  }

  @Test public void t11() {
    zoomingInto("double  t; t =a * 1L;").gives("double  t; t =(long) a;");
  }

  @Test public void t12() {
    zoomingInto("double  t; t =1L * 9 * a;").gives("double  t; t =(long) 9 * a;");
  }

  @Test public void t13() {
    zoomingInto("double  t; t =1L * 9 * a * b;").gives("double  t; t =(long) 9 * a * b;");
  }

  @Test public void t14() {
    zoomingInto("double  t; t =9 * a * 1L * b;").gives("double  t; t =(long) 9 * a * b;");
  }
}
