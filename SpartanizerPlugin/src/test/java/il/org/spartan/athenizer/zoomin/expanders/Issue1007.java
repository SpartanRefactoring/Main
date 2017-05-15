package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

/** Test case for @link MultiplicationToDoubleCast} covers also Issue #1006
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-01-11 */
@SuppressWarnings("static-method")
public class Issue1007 {
  @Test public void t1() {
    bloatingOf("double  t; t = 2 * 1.0;")//
        .gives("double t; t = (double) 2;");
  }
  @Test public void t10() {
    bloatingOf("double  t; t =1L * a;")//
        .gives("double  t; t =(long) a;");
  }
  @Test public void t11() {
    bloatingOf("double  t; t =a * 1L;")//
        .gives("double  t; t =(long) a;");
  }
  @Test public void t12() {
    bloatingOf("double  t; t =1L * 9 * a;")//
        .gives("double  t; t =(long) 9 * a;");
  }
  @Test public void t13() {
    bloatingOf("double  t; t =1L * 9 * a * b;")//
        .gives("double  t; t =(long) 9 * a * b;");
  }
  @Test public void t14() {
    bloatingOf("double  t; t =9 * a * 1L * b;")//
        .gives("double  t; t =(long) 9 * a * b;");
  }
  @Test public void t2() {
    bloatingOf("double  t; t = 1.0 * a;")//
        .gives("double  t; t =(double) a;");
  }
  @Test public void t3() {
    bloatingOf("double  t; t =1. * a;")//
        .gives("double  t; t =(double) a;");
  }
  @Test public void t4() {
    bloatingOf("double  t; t =a * 1.;")//
        .gives("double  t; t =(double) a;");
  }
  @Test public void t5() {
    bloatingOf("double  t; t =1. * 9 * a;")//
        .gives("double  t; t =(double) 9 * a;");
  }
  @Test public void t6() {
    bloatingOf("double  t; t =1. * 9 * a * b;")//
        .gives("double  t; t =(double) 9 * a * b;");
  }
  @Test public void t7() {
    bloatingOf("double  t; t =9 * a * 1. * b;")//
        .gives("double  t; t =(double) 9 * a * b;");
  }
  @Test public void t8() {
    bloatingOf("double  t; t = 2 * 1L;")//
        .gives("double t; t = (long) 2;");
  }
  @Test public void t9() {
    bloatingOf("double  t; t = 1L * a;")//
        .gives("double  t; t =(long) a;");
  }
}
