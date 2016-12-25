package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Unit tests for {@link VariableDeclarationStatementSplit}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class VariableDeclarationStatementSplitTest {
  @Test public void a() {
    expandingOf("int a = 3, b = 4, c = 5;")
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
    expandingOf("int a = 3, b;").stays();
  }

  @Test public void c() {
    expandingOf("int a, b, c;").stays();
  }
}
