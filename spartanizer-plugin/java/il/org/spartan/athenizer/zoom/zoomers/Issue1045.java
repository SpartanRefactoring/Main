package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;

/** Unit tests for {@link ParenthesesBloater} Issue #1045
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-11 */
@SuppressWarnings("static-method")
public class Issue1045 {
  @Test public void a() {
    bloatingOf("while(true) {a = b + c > d;}") //
        .gives("while(true) {a = (b + c) > d;}") //
        .stays() //
    ;
  }
  @Test public void b() {
    bloatingOf("if (a + b + c > 3) { return 1; }")//
        .gives("if ((a + b + c) > 3) { return 1; }");
  }
  @Test public void c() {
    bloatingOf("while (a + b + c > 4) { }")//
        .gives("while ((a + b + c) > 4) { }");
  }
}
