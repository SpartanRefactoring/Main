package il.org.spartan.spartanizer.ast.engine;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** see Issue #831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
public class Issue831 {
  protected class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(final MethodDeclaration method) {
      super(method);
    }

    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }

  MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; }");

  @Test public void statementsInScannerAreUndefinedWhenMethodDoesNotHaveBody() {
    assertTrue(new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);")).availableStatements() == null);
  }

  @Test public void noStatementsInScannerWhenMethodHasEmptyBody() {
    assertTrue(new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a){}")).availableStatements().isEmpty());
  }

  @Test public void oneStatementInScanner() {
    assertTrue("int a;\n".equals(new MethodScannerIExt(oneStatMethod).availableStatements().get(0) + ""));
  }

  @Test public void fourStatementInScanner() {
    String body = "";
    for (final Statement iter : new MethodScannerIExt(fourStatMethod).statements())
      body += iter + "";
    assertTrue("int a;\nint b;\nint c;\nint d;\n".equals(body));
  }

  @Test public void givenNullinsteadMethodAssertionFailure() {
    try {
      new MethodScannerIExt(null).hashCode();
    } catch (final Error e) {
      e.getClass();
      assertTrue(true);
      return;
    }
    assertFalse(true);
  }
}
