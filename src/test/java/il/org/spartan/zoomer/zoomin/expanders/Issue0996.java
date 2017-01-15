package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.inflate.zoomers.BoatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.inflate.zoomers.*;

/** Unit tests for {@link DeclarationWithInitExpander}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0996 {
  @Test public void a() {
    bloatingOf("int a = 0;")//
        .gives("int a;" //
            + "a = 0;")
        .stays();
  }

  @Test public void b() {
    bloatingOf("int a = f();")//
        .gives("int a;" //
            + "a = f();")
        .stays();
  }

  @Test public void c() {
    bloatingOf("final String[] command = { \"/bin/bash\", \"-c\", shellCommand };")//
        .stays();
  }

  @Test public void d() {
    bloatingOf("@SuppressWarnings(\"unchecked\") int a = f();")//
        .stays();
  }
}
