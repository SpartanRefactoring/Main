package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.*;

/** see Issue #831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
@SuppressWarnings("static-method")
public class Issue0831 {
  final MethodDeclaration oneStatMethod = (MethodDeclaration) make.ast("public void foo() {int a; }");
  final MethodDeclaration fourStatMethod = (MethodDeclaration) make.ast("public void foo() {int a; int b; int c; int d; }");

  @Test public void fourStatementInScanner() {
    String body = "";
    for (final Statement iter : new MethodScannerIExt(fourStatMethod).statements()) // NANO
      body += iter + "";
    azzert.that(body, is("int a;\nint b;\nint c;\nint d;\n"));
  }
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
    assert new MethodScannerIExt((MethodDeclaration) make.ast("public int a(String a){}")).availableStatements().isEmpty();
  }
  @Test public void oneStatementInScanner() {
    assert "int a;\n".equals(the.firstOf(new MethodScannerIExt(oneStatMethod).availableStatements()) + "");
  }
  @Test public void statementsInScannerAreUndefinedWhenMethodDoesNotHaveBody() {
    assert new MethodScannerIExt((MethodDeclaration) make.ast("public int a(String a);")).availableStatements() == null;
  }

  protected static class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(final MethodDeclaration method) {
      super(method);
    }
    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }
}
