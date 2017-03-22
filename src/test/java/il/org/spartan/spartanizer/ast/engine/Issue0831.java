package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** see Issue #831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
@SuppressWarnings("static-method")
public class Issue0831 {
  @Nullable final MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  @Nullable final MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; }");

  @Test public void fourStatementInScanner() {
    @NotNull String body = "";
    for (final Statement iter : new MethodScannerIExt(fourStatMethod).statements()) // NANO
      body += iter + "";
    azzert.that(body, is("int a;\nint b;\nint c;\nint d;\n"));
  }

  @Test public void givenNullinsteadMethodAssertionFailure() {
    try {
      new MethodScannerIExt(null).hashCode();
    } catch (@NotNull final Error ¢) {
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

  protected static class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(@NotNull final MethodDeclaration method) {
      super(method);
    }

    @Nullable @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }
}
