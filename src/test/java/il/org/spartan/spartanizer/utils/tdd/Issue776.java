package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;


/** 
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 16-11-7 */
@SuppressWarnings("static-method") public class Issue776 {
  @Test public void shouldReturnFalseForBooleanLiteral() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{}"))), 0);
  }
  
  
}
