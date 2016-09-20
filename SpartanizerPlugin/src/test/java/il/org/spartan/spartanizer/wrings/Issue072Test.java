package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue072Test {
  @Test public void issue72ma() {
    final String s = "0-x";
    final InfixExpression i = into.i(s);
    azzert.that(i, iz(s));
    azzert.that(left(i), iz("0"));
    azzert.that(right(i), iz("x"));
    assert !i.hasExtendedOperands();
    assert iz.literal0(left(i));
    assert !iz.literal0(right(i));
    azzert.that(make.minus(left(i)), iz("0"));
    azzert.that(make.minus(right(i)), iz("-x"));
    trimmingOf(s).gives("-x");
  }

  @Test public void issue72mb() {
    trimmingOf("x-0").gives("x");
  }

  @Test public void issue72mc() {
    trimmingOf("x-0-y").gives("x-y").stays();
  }

  @Test public void issue72md1() {
    trimmingOf("0-x-0").gives("-x").stays();
  }

  @Test public void issue72md2() {
    trimmingOf("0-x-0-y").gives("-x-y").stays();
  }

  @Test public void issue72md3() {
    trimmingOf("0-x-0-y-0-z-0-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void issue72me() {
    trimmingOf("0-(x-0)").gives("-(x-0)").gives("-(x)").stays();
  }

  @Test public void issue72me1() {
    assert !iz.negative(into.e("0"));
  }

  @Test public void issue72me2() {
    assert iz.negative(into.e("-1"));
    assert !iz.negative(into.e("+1"));
    assert !iz.negative(into.e("1"));
  }

  @Test public void issue72me3() {
    assert iz.negative(into.e("-x"));
    assert !iz.negative(into.e("+x"));
    assert !iz.negative(into.e("x"));
  }

  @Test public void issue72meA() {
    trimmingOf("(x-0)").gives("(x)").stays();
  }

  @Test public void issue72mf1() {
    trimmingOf("0-(x-y)").gives("-(x-y)").stays();
  }

  @Test public void issue72mf1A() {
    trimmingOf("0-(x-0)")//
        .gives("-(x-0)")//
        .gives("-(x)") //
        .stays();
  }

  @Test public void issue72mf1B() {
    assert iz.simple(into.e("x"));
    trimmingOf("-(x-0)")//
        .gives("-(x)")//
        .stays();
  }

  @Test public void issue72mg() {
    trimmingOf("(x-0)-0").gives("(x)").stays();
  }

  @Test public void issue72mg1() {
    trimmingOf("-(x-0)-0").gives("-(x)").stays();
  }

  @Test public void issue72mh() {
    trimmingOf("x-0-y").gives("x-y").stays();
  }

  @Test public void issue72mi() {
    trimmingOf("0-x-0-y-0-z-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void issue72mj() {
    trimmingOf("0-0").gives("0");
  }

  @Test public void issue72mx() {
    trimmingOf("0-0").gives("0");
  }

  @Test public void issue72pa() {
    trimmingOf("(int)x+0").gives("(int)x");
  }

  @Test public void issue72pb() {
    trimmingOf("0+(int)x").gives("(int)x");
  }

  @Test public void issue72pc() {
    trimmingOf("0-x").gives("-x");
  }

  @Test public void issue72pd() {
    trimmingOf("0+(int)x+0").gives("(int)x").stays();
  }

  @Test public void issue72pe() {
    trimmingOf("(int)x+0-x").gives("(int)x-x").stays();
  }

  @Test public void issue72pf() {
    trimmingOf("(int)x+0+(int)x+0+0+(int)y+0+0+0+0+(int)z+0+0").gives("(int)x+(int)x+(int)y+(int)z").stays();
  }

  @Test public void issue72pg() {
    trimmingOf("0+(x+y)").gives("0+x+y").stays();
  }

  @Test public void issue72ph() {
    trimmingOf("0+((x+y)+0+(z+h))+0")//
    .gives("0 +(x+y) +0+(z+h)+0")//
    .gives("0 +x+y +0+(z+h)+0")//
    .stays();
  }
  @Test public void issue72pi() {
    trimmingOf("0+(0+x+y+((int)x+0))")//
        .gives("0+0+x+y+((int)x +0)")//
        .gives("0+0+x+y+((int)x)") //
        .gives("0+0+x+y+(int)x") //
        .stays();
  }
}
