package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit tests for {@link VariableDeclarationStatementSplit}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0968 {
  @Test public void a() {
    bloatingOf("int a = 3, b = 4, c = 5;")
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
    bloatingOf("int a = 3, b;")//
        .stays();
  }
  @Test public void c() {
    bloatingOf("int a, b, c;")//
        .stays();
  }
}
