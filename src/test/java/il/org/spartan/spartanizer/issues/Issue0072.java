package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests for {@Link InfixAdditionZero}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0072 {
  @Test public void ma() {
    @NotNull final String s = "0-x";
    @NotNull final InfixExpression i = into.i(s);
    azzert.that(i, iz(s));
    azzert.that(left(i), iz("0"));
    azzert.that(right(i), iz("x"));
    assert !i.hasExtendedOperands();
    assert iz.literal0(left(i));
    assert !iz.literal0(right(i));
    azzert.that(make.minus(left(i)), iz("0"));
    azzert.that(make.minus(right(i)), iz("-x"));
    trimmingOf(s)//
        .gives("-x");
  }

  @Test public void mb() {
    trimmingOf("x-0")//
        .gives("x");
  }

  @Test public void mc() {
    trimmingOf("x-0-y")//
        .gives("x-y")//
        .stays();
  }

  @Test public void md1() {
    trimmingOf("0-x-0")//
        .gives("-x")//
        .stays();
  }

  @Test public void md2() {
    trimmingOf("0-x-0-y")//
        .gives("-x-y")//
        .stays();
  }

  @Test public void md3() {
    trimmingOf("0-x-0-y-0-z-0-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void me() {
    trimmingOf("0-(x-0)")//
        .gives("-(x-0)")//
        .gives("-(x)")//
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
    trimmingOf("(x-0)")//
        .gives("(x)")//
        .stays();
  }

  @Test public void mf1() {
    trimmingOf("0-(x-y)")//
        .gives("-(x-y)")//
        .stays();
  }

  @Test public void mf1A() {
    trimmingOf("0-(x-0)")//
        .gives("-(x-0)")//
        .gives("-(x)")//
        .stays();
  }

  @Test public void mf1B() {
    assert iz.simple(into.e("x"));
    trimmingOf("-(x-0)")//
        .gives("-(x)")//
        .stays();
  }

  @Test public void mg() {
    trimmingOf("(x-0)-0")//
        .gives("(x)")//
        .stays();
  }

  @Test public void mg1() {
    trimmingOf("-(x-0)-0")//
        .gives("-(x)")//
        .stays();
  }

  @Test public void mh() {
    trimmingOf("x-0-y")//
        .gives("x-y")//
        .stays();
  }

  @Test public void mi() {
    trimmingOf("0-x-0-y-0-z-0")//
        .gives("-x-y-z")//
        .stays();
  }

  @Test public void mj() {
    trimmingOf("0-0")//
        .gives("0");
  }

  @Test public void mx() {
    trimmingOf("0-0")//
        .gives("0");
  }

  @Test public void pa() {
    trimmingOf("(int)x+0")//
        .gives("(int)x");
  }

  @Test public void pb() {
    trimmingOf("0+(int)x")//
        .gives("(int)x");
  }

  @Test public void pc() {
    trimmingOf("0-x")//
        .gives("-x");
  }

  @Test public void pd() {
    trimmingOf("0+(int)x+0")//
        .gives("(int)x")//
        .stays();
  }

  @Test public void pe() {
    trimmingOf("(int)x+0-x")//
        .gives("(int)x-x")//
        .stays();
  }

  @Test public void pf() {
    trimmingOf("(int)x+0+(int)x+0+0+(int)y+0+0+0+0+(int)z+0+0")//
        .gives("(int)x+0+(int)x+0+0+(int)y+0+0+0+0+(int)z+0").gives("(int)x+(int)x+(int)y+(int)z")//
        .stays();
  }

  @Test public void pg() {
    trimmingOf("0+(x+y)")//
        .gives("0+x+y")//
        .stays();
  }

  @Test public void ph() {
    trimmingOf("0+((x+y)+0+(z+h))+0")//
        .gives("0 +(x+y) +0+(z+h)+0")//
        .gives("0 +x+y +0+(z+h)+0")//
        .stays();
  }

  @Test public void pi() {
    trimmingOf("0+(0+x+y+((int)x+0))")//
        .gives("0+x+y+(int)x +0")//
        .stays();
  }
}
