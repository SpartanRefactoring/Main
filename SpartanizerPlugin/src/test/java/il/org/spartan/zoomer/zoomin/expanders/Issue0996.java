package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit tests for {@link DeclarationWithInitExpander}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0996 {
  @Test public void a() {
    zoomingInto("int a = 0;")//
        .gives("int a;" //
            + "a = 0;")
        .stays();
  }

  @Test public void b() {
    zoomingInto("int a = f();")//
        .gives("int a;" //
            + "a = f();")
        .stays();
  }

  @Test public void c() {
    zoomingInto("final String[] command = { \"/bin/bash\", \"-c\", shellCommand };")//
 .stays();
  }

  @Test public void d() {
    zoomingInto("@SuppressWarnings(\"unchecked\") int a = f();")//
 .stays();
  }
}
