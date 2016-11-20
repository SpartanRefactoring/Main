package il.org.spartan.spartanizer.ast.engine;

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
    assert new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);")).availableStatements() == null;
  }

  @Test public void noStatementsInScannerWhenMethodHasEmptyBody() {
    assert new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a){}")).availableStatements().isEmpty();
  }

  @Test public void oneStatementInScanner() {
    assert "int a;\n".equals(new MethodScannerIExt(oneStatMethod).availableStatements().get(0) + "");
  }

  @Test public void fourStatementInScanner() {
    String body = "";
    for (final Statement iter : new MethodScannerIExt(fourStatMethod).statements())
      body += iter + "";
    assert "int a;int b;int c;int d;".equals(body);
  }

  // TODO: Adi - note obfuscated code assert (false), instead of fail().
  // Other examples occur in other functions.
  @Test public void givenNullinsteadMethodAssertionFailure() {
    try {
      new MethodScannerIExt(null).hashCode();
    } catch (final Error e) {
      e.getClass();
      return;
    }
    assert false;
  }
}
