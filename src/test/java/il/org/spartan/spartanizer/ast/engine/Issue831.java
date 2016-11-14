package il.org.spartan.spartanizer.ast.engine;

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
@SuppressWarnings({ "javadoc" })
public class Issue831 {
  protected class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(MethodDeclaration method) {
      super(method);
    }

    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }

  MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");
  MethodDeclaration oneStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; }");
  MethodDeclaration fourStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; ; ; ; }");

  @Test public void statementsInScannerAreUndefinedWhenMethodDoesNotHaveBody() {
    assertTrue((new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);"))).availableStatements() == null);
  }

  @Test public void noStatementsInScannerWhenMethodHasEmptyBody() {
    assertTrue((new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a){}"))).availableStatements().isEmpty());
  }

  @Test public void oneStatementInScanner() {
   assertTrue("int a;\n".equals((new MethodScannerIExt(oneStatMethod).availableStatements().get(0) + "")));
  }
}
