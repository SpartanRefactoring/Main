package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

/** Test Class for Expand boolean expressions
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @author tomerdragucki
 * @since 2017-01-13 */
@SuppressWarnings("static-method")
public class Issue0980 {
  @Test public void test00() {
    bloatingOf("return a && b;")//
        .stays();
  }
  @Test public void test0() {
    bloatingOf("return x() && y();")//
        .gives("boolean a = x(); boolean b = y(); return a && b;");
  }
  @Test public void test1() {
    bloatingOf("return true && y();")//
        .gives("boolean a = true; boolean b = y(); return a&&b;");
  }
  @Test public void test2() {
    bloatingOf("boolean t = x && y();")//
        .gives("boolean t; t = x && y();")//
        .gives("boolean t; boolean a = x; boolean b = y(); t = a&&b;");
  }
  @Test public void test3() {
    bloatingOf("return x || y();")//
        .gives("boolean a = x; boolean b = y(); return a || b;");
  }
  @Test public void test4() {
    bloatingOf("return true || y();")//
        .gives("boolean a = true; boolean b = y(); return a || b;");
  }
  @Test public void test5() {
    bloatingOf("boolean t =  x || y();")//
        .gives("boolean t; t =  x || y();")//
        .gives("boolean t; boolean a = x; boolean b = y(); t = a || b;");
  }
  @Test public void test6() {
    bloatingOf("return x && y() || z;")//
        .gives("boolean a = x && y(); boolean b = z; return a || b;")//
        .gives("boolean a; a = x && y(); boolean b = z; return a || b;")
        .gives("boolean a; boolean a2 = x;boolean b2 = y(); a = a2 && b2; boolean b = z; return a || b;");
  }
}
