package il.org.spartan.spartanizer.ast.engine;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** see Issue #7831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
@SuppressWarnings({ "javadoc" })
public class Issue831 {
  protected class MethodScannerIExt extends MethodScanner {
    public MethodScannerIExt(final MethodDeclaration method) {
      super(method);
    }

    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }

  @Test public void statementsInScannerAreUndefinedWhenMethodDoesNotHaveBody() {
    assertTrue(new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);")).availableStatements() == null);
  }

  @Test public void noStatementsInScannerWhenMethodHasEmptyBody() {
    assertTrue(new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a){}")).availableStatements().isEmpty());
  }
  
  @Test public void givenNullinsteadMethodAssertionFailure() {
    try {
      new MethodScannerIExt(null).hashCode();
    } catch (Error e) {
      e.getClass();
      assertTrue(true);
      return;
    }
    assertFalse(true);
  }

  
}
