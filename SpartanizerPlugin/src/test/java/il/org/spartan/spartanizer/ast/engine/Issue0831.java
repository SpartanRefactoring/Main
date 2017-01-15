package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.lisp.*;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** see Issue #831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
public class Issue0831 {
  protected class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(final MethodDeclaration method) {
      super(method);
    }

    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }

  final MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  final MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; }");

  @Test public void fourStatementInScanner() {
    String body = "";
    for (final Statement iter : new MethodScannerIExt(fourStatMethod).statements())
      body += iter + "";
    assertEquals("int a;\nint b;\nint c;\nint d;\n", body);
  }

  // TODO: Adi - note obfuscated code assert (false), instead of fail().
  // Other examples occur in other functions.
  @Test public void givenNullinsteadMethodAssertionFailure() {
    try {
      new MethodScannerIExt(null).hashCode();
    } catch (final Error ¢) {
      ¢.getClass();
      return;
    }
    assert false;
  }

  @Test public void noStatementsInScannerWhenMethodHasEmptyBody() {
    assert new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a){}")).availableStatements().isEmpty();
  }

  @Test public void oneStatementInScanner() {
    assert "int a;\n".equals(first(new MethodScannerIExt(oneStatMethod).availableStatements()) + "");
  }

  @Test public void statementsInScannerAreUndefinedWhenMethodDoesNotHaveBody() {
    assert new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);")).availableStatements() == null;
  }
}
