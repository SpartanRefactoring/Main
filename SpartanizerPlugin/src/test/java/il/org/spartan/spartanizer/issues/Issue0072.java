package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests for {@Link InfixAdditionZero}
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0072 {
  @Test public void a() {
    trimminKof("(x-0)-0")//
        .gives("(x-0)")//
        .gives("(x)")//
        .gives("x")//
        .stays();
  }

  @Test public void a1() {
    trimminKof("-(x-0)")//
        .gives("-(x)")//
        .gives("-x")//
        .stays();
  }

  @Test public void ma() {
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
    trimminKof(s)//
        .gives("-x");
  }

  @Test public void mb() {
    trimminKof("x-0")//
        .gives("x");
  }

  @Test public void mc() {
    trimminKof("x-0-y")//
        .gives("x-y")//
        .stays();
  }

  @Test public void md1() {
    trimminKof("0-x-0")//
        .gives("-x")//
        .stays();
  }

  @Test public void md2() {
    trimminKof("0-x-0-y")//
        .gives("-x-y")//
        .stays();
  }

  @Test public void md3() {
    trimminKof("0-x-0-y-0-z-0-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void me() {
    trimminKof("0-(x-0)")//
        .gives("-(x-0)")//
        .gives("-(x)")//
        .gives("-x")//
        .stays();
  }

  @Test public void me1() {
    assert !iz.negative(into.e("0"));
  }

  @Test public void me2() {
    assert iz.negative(into.e("-1"));
    assert !iz.negative(into.e("+1"));
    assert !iz.negative(into.e("1"));
  }

  @Test public void me3() {
    assert iz.negative(into.e("-x"));
    assert !iz.negative(into.e("+x"));
    assert !iz.negative(into.e("x"));
  }

  @Test public void meA() {
    trimminKof("(x-0)")//
        .gives("(x)")//
        .gives("x")//
        .stays();
  }

  @Test public void mf1() {
    trimminKof("0-(x-y)")//
        .gives("-(x-y)")//
        .stays();
  }

  @Test public void mf1A() {
    trimminKof("0-(x-0)")//
        .gives("-(x-0)")//
        .gives("-(x)")//
        .gives("-x")//
        .stays();
  }

  @Test public void mf1B() {
    assert iz.simple(into.e("x"));
    trimminKof("-(x-0)")//
        .gives("-(x)")//
        .gives("-x")//
        .stays();
  }

  @Test public void mh() {
    trimminKof("x-0-y")//
        .gives("x-y")//
        .stays();
  }

  @Test public void mi() {
    trimminKof("0-x-0-y-0-z-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void mj() {
    trimminKof("0-0")//
        .gives("0");
  }

  @Test public void mx() {
    trimminKof("0-0")//
        .gives("0");
  }

  @Test public void pa() {
    trimminKof("(int)x+0")//
        .gives("(int)x");
  }

  @Test public void pb() {
    trimminKof("0+(int)x")//
        .gives("(int)x");
  }

  @Test public void pc() {
    trimminKof("0-x")//
        .gives("-x");
  }

  @Test public void pd() {
    trimminKof("0+(int)x+0")//
        .gives("(int)x")//
        .stays();
  }

  @Test public void pe() {
    trimminKof("(int)x+0-x")//
        .gives("(int)x-x")//
        .stays();
  }

  @Test public void pf() {
    trimminKof("(int)x+0+(int)x+0+0+(int)y+0+0+0+0+(int)z+0+0")//
        .gives("(int)x+0+(int)x+0+0+(int)y+0+0+0+0+(int)z+0").gives("(int)x+(int)x+(int)y+(int)z")//
        .stays();
  }

  @Test public void pg() {
    trimminKof("0+(x+y)")//
        .gives("0+x+y")//
        .stays();
  }

  @Test public void ph() {
    trimminKof("0+((x+y)+0+(z+h))+0")//
        .gives("0 +(x+y) +0+(z+h)+0")//
        .gives("0 +x+y +0+(z+h)+0")//
        .stays();
  }

  @Test public void pi() {
    trimminKof("0+(0+x+y+((int)x+0))")//
        .gives("0+x+y+(int)x +0")//
        .stays();
  }
}
