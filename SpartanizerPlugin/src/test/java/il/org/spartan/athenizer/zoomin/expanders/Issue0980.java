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
        .gives("boolean b3=x();boolean b4=y();return b3&&b4;") //
    ;
  }
  @Test public void test1() {
    bloatingOf("return true && y();")//
        .gives("boolean b3=true;boolean b4=y();return b3&&b4;") //
    ;
  }
  @Test public void test2() {
    bloatingOf("boolean t = x && y();")//
        .gives("boolean t; t = x && y();")//
        .gives("boolean t;boolean b3=x;boolean b4=y();t=b3&&b4;") //
    ;
  }
  @Test public void test3() {
    bloatingOf("return x || y();")//
        .gives("boolean b3=x;boolean b4=y();return b3||b4;") //
    ;
  }
  @Test public void test4() {
    bloatingOf("return true || y();")//
        .gives("boolean b3=true;boolean b4=y();return b3||b4;") //
    ;
  }
  @Test public void test5() {
    bloatingOf("boolean t =  x || y();")//
        .gives("boolean t; t =  x || y();")//
        .gives("boolean t;boolean b3=x;boolean b4=y();t=b3||b4;") //
    ;
  }
  @Test public void test6() {
    bloatingOf("return x && y() || z;")//
        .gives("boolean b3=x&&y();boolean b4=z;return b3||b4;") //
        .gives("boolean b3;b3=x&&y();boolean b4;b4=z;return b3||b4;") //
        .gives("boolean b3;boolean b5=x;boolean b6=y();b3=b5&&b6;boolean b4;b4=z;return b3||b4;") //
    ;
  }
}
