package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.testing.TipperTest;
import il.org.spartan.spartanizer.tippers.InfixAdditionZero;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Test for issue 408
 * @author Matteo Orru'
 * @author Niv Shalmon
 * @since 2016 */
@Ignore("Take note, you cannot convert x + 0 to x, unless you know that x is not a string")
public class Issue0408 extends TipperTest<InfixExpression> {
  @Override public Tipper<InfixExpression> tipper() {
    return new InfixAdditionZero();
  }
  @Override public Class<InfixExpression> tipsOn() {
    return InfixExpression.class;
  }
  @Test public void t01() {
    trimmingOf("0+x")//
        .stays();
  }
  @Test public void t02() {
    trimmingOf("0+(0+x+y+(4))")//
        .stays();
  }
  @Test public void t02b() {
    trimmingOf("(0+x)+y")//
        .stays();
  }
  @Test public void t02c() {
    trimmingOf("int x; return (0+x)+y;")//
        .gives("int x; return (x)+y");
  }
  @Test public void t03() {
    trimmingOf("0+x+y+4")//
        .stays();
  }
  @Test public void t033() {
    trimmingOf("int x; return 0+x+y+4+z+5;")//
        .gives("int x; return x+y+4+z+5;")//
        .stays();
  }
  @Test public void t03b() {
    trimmingOf("0+x+y+4+5")//
        .stays();
  }
  @Test public void t03b1() {
    trimmingOf("int x,y; return 0+(x + 0 + y + (x + 2));")//
        .gives("int x,y; return (x+y+(x+2));")//
        .stays();
  }
  @Test public void t03c() {
    trimmingOf("0+x+y+4+z")//
        .stays();
  }
  @Test public void t03e() {
    trimmingOf("int x; return 0+x+0+y+0;")//
        .gives("int x; return x+y+0;")//
        .stays();
  }
  @Test public void t03f() {
    trimmingOf("int y; return 0+x+0+y+0;")//
        .stays();
  }
  @Test public void t03g() {
    trimmingOf("int x; return 0+x+0+0+0+y+4;")//
        .gives("int x; return x+y+4;")//
        .stays();
  }
  @Test public void t04() {
    trimmingOf("x+0")//
        .stays();
  }
  @Test public void t04b() {
    trimmingOf("int x; return x+0;")//
        .gives("int x; return x;")//
        .stays();
  }
  @Test public void t05() {
    trimmingOf("0+x+3")//
        .stays();
  }
  @Test public void t06() {
    trimmingOf("x+0+y")//
        .stays();
  }
  @Test public void t06b() {
    trimmingOf("int x; return x+0+y;")//
        .gives("int x; return x+y;")//
        .stays();
  }
  @Test public void t07() {
    trimmingOf("0+0+x+4*y")//
        .stays();
  }
  @Test public void t07b() {
    trimmingOf("int x; return 0+0+x+4*y;")//
        .gives("int x; return x+4*y")//
        .stays();
  }
}
