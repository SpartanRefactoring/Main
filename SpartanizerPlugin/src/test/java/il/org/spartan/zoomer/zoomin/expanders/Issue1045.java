package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit tests for {@link ParenthesesBloater} Issue #1045
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-11 */
@SuppressWarnings("static-method")
public class Issue1045 {
  @Test public void a() {
    bloatingOf("if (a > 1 || b > 2 || c + e > 3) { return 1; }")//
        .gives("if ((a > 1 || b > 2) || c + e > 3) { return 1; }").gives("if (((a > 1) || b > 2) || c + e > 3) { return 1; }")//
        .gives("if (((a > 1) || (b > 2)) || c + e > 3) { return 1; }").gives("if (((a > 1) || (b > 2)) || (c + e > 3)) { return 1; }")//
        .gives("if (((a > 1) || (b > 2)) || ((c + e) > 3)) { return 1; }");
  }

  @Test public void b() {
    bloatingOf("if (a + b + c > 3) { return 1; }")//
        .gives("if ((a + b + c) > 3) { return 1; }");
  }

  @Test public void c() {
    bloatingOf("while (a + b + c > 4) { }")//
        .gives("while ((a + b + c) > 4) { }");
  }

  @Test public void d() {
    bloatingOf("while (a > 1 && a < 2 || c > 3) { }")//
        .gives("while ((a > 1 && a < 2) || c > 3) { }")//
        .gives("while (((a > 1) && a < 2) || c > 3) { }").gives("while (((a > 1) && (a < 2)) || c > 3) { }")//
        .gives("while (((a > 1) && (a < 2)) || (c > 3)) { }");
  }
}
