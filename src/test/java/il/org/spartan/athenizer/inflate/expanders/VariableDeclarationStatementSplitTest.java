package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.zoomer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.expanders.*;

/** Unit tests for {@link VariableDeclarationStatementSplit}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@Ignore
@SuppressWarnings("static-method")
public class VariableDeclarationStatementSplitTest {
  @Test public void a() {
    expansionOf("int a = 3, b = 4, c = 5;")
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
    expansionOf("int a = 3, b;").stays();
  }

  @Test public void c() {
    expansionOf("int a, b, c;").stays();
  }
}
