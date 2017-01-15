package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit tests for {@link VariableDeclarationStatementSplit}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@Ignore // TODO Tomer Tragucki
@SuppressWarnings("static-method")
public class Issue0968 {
  @Test public void a() {
    zoomingInto("int a = 3, b = 4, c = 5;")
        .gives("int a = 3;" //
            + "int b = 4, c = 5;")
        .gives("int a;" //
            + "a = 3;" //
            + "int b = 4, c = 5;")
        .gives("int a;" //
            + "a = 3;" //
            + "int b = 4;" //
            + "int c = 5;")
        .gives("int a;" //
            + "a = 3;" //
            + "int b;" //
            + "b = 4;" //
            + "int c = 5;")
        .gives("int a;" //
            + "a = 3;" //
            + "int b;" //
            + "b = 4;" //
            + "int c;" //
            + "c = 5;")
        .stays();
  }

  @Test public void b() {
    zoomingInto("int a = 3, b;")//
 .stays();
  }

  @Test public void c() {
    zoomingInto("int a, b, c;")//
 .stays();
  }
}
