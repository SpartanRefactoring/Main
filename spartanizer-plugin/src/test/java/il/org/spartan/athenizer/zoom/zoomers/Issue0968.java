package il.org.spartan.athenizer.zoom.zoomers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit tests for {@link VariableDeclarationStatementSplit}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0968 extends BloaterTest<VariableDeclarationStatement> {
  @Override public Tipper<VariableDeclarationStatement> bloater() {
    return new VariableDeclarationStatementSplit();
  }
  @Override public Class<VariableDeclarationStatement> tipsOn() {
    return VariableDeclarationStatement.class;
  }
  @Test public void a() {
    bloatingOf("int a = 3, b = 4, c = 5;")//
        .gives("int a = 3; int b = 4, c = 5;")//
        .gives("int a = 3;int b = 4;int c = 5;")//
        .stays();
  }
  @Test public void aWithAllBloaters() {
    TestUtilsBloating.bloatingOf("int a = 3, b = 4, c = 5;")//
        .gives("int a = 3; int b = 4, c = 5;")//
        .gives("int a; a = 3;int b = 4;int c = 5;")//
        .gives("int a;a = 3;int b;b = 4;int c;c = 5;")//
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
  @Test public void d() {
    bloatingOf("final int a = 3, b = 4;")//
        .gives("final int a = 3; final int b = 4;");
  }
}
