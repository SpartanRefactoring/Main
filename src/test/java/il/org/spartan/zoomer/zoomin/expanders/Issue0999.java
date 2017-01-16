package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link AssignmentAndAssignment}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
// TODO: post a link to the tested class
@SuppressWarnings("static-method")
public class Issue0999 {
  @Test public void a() {
    bloatingOf("a = b = 3;")//
        .gives("b = 3; a = b;");
  }

  @Test public void b() {
    bloatingOf("a = b = c = 3;")//
        .gives("c = 3; a = b = c;")//
        .gives("c = 3; b = c; a = b;")//
        .stays();
  }

  @Test public void c() {
    bloatingOf("a += b += 3;")//
        .gives("b += 3; a += b;");
  }
}
