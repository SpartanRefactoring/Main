package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import static org.junit.Assert.*;

import il.org.spartan.spartanizer.utils.tdd.*;

/** see Issue #717 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-05 */
@SuppressWarnings("static-method") public class Issue717 {
  
  MethodDeclaration fiveStatMethod = (MethodDeclaration) wizard.ast("public void foo() {int a; int b; int c; int d; int e;}");

  @Test public void isCompiled() {
    assert true;
  }
  
  @Test  public void nullCheckReturnsFalse(){
    assertFalse (determineIf.hasBigBlock(null));
  }


  @Test public void containsBigBlock() {
    assertTrue(determineIf.hasBigBlock(fiveStatMethod));
  }

}