package il.org.spartan.athenizer.zoom.zoomers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test Class for Expand boolean expressions
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @author tomerdragucki
 * @since 2017-01-13 */
@Ignore
public class Issue0980 extends BloaterTest<InfixExpression> {
  @Override public Tipper<InfixExpression> bloater() {
    return new BooleanExpressionBloater();
  }
  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }
  @Test public void test00() {
    bloatingOf("return a && b;")//
        .stays();
  }
  @Test public void test0() {
    bloatingOf("return x() && y();")//
        .gives("boolean a=x();boolean b=y();return a&&b;") //
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
  @Test public void test7() {
    bloatingOf("return λ -> c1.holds(λ) || c2.holds(λ);")//
        .stays()//
    ;
  }
}
