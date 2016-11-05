package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") public class Issue717 {
  
  MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");
  
  @Test public void a() {
    assert true;
  }
  
  @Test public void containsBigBlock() {
    assertTrue(determineIf.hasBigBlock(fiveStatMethod));
  }
  
  
  
}