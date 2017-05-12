package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Test for issue 408
 * @author Matteo Orru'
 * @since 2016 */
@Ignore("Take note, you cannot convert x + 0 to x, unless you know that x is not a string")
@SuppressWarnings("static-method")
public class Issue0408 {
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
        .gives("0+x+y")//
        .stays();
  }

  @Test public void t03() {
    trimmingOf("0+x+y+4")//
        .gives("x+y+4")//
        .stays();
  }

  @Test public void t033() {
    trimmingOf("0+x+y+4+z+5")//
        .gives("x+y+4+z+5")//
        .stays();
  }

  @Test public void t03b() {
    trimmingOf("0+x+y+4+5")//
        .gives("x+y+9")//
        .stays();
  }

  @Test public void t03b1() {
    trimmingOf("0+(x + 0 + y + (x + 2))")//
        .gives("x+y+x+2")//
        .stays();
  }

  @Test public void t03c() {
    trimmingOf("0+x+y+4+z")//
        .gives("x+y+4+z")//
        .stays();
  }

  @Test public void t03d() {
    trimmingOf("0+x+y+4+z+w")//
        .gives("x+y+4+z+w")//
        .stays();
  }

  @Test public void t03e() {
    trimmingOf("0+x+0+y")//
        .gives("x+y")//
        .stays();
  }

  @Test public void t03f() {
    trimmingOf("0+x+0+y+4")//
        .gives("x+y+4")//
        .stays();
  }

  @Test public void t03g() {
    trimmingOf("0+x+0+0+0+y+4")//
        .gives("x+y+4")//
        .stays();
  }

  @Test public void t04() {
    trimmingOf("x+0")//
        .gives("x")//
        .stays();
  }

  @Test public void t05() {
    trimmingOf("0+x+3")//
        .gives("x+3")//
        .stays();
  }

  @Test public void t06() {
    trimmingOf("x+0+y")//
        .gives("x+y")//
        .stays();
  }

  @Test public void t07() {
    trimmingOf("0+(0+x+y+(4))")//
        .gives("x+y+4")//
        .stays();
  }

  @Test public void t08() {
    trimmingOf("0+0+x+4*y")//
        .gives("x+4*y")//
        .stays();
  }
}
