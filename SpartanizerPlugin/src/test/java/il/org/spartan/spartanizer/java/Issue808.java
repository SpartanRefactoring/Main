
package il.org.spartan.spartanizer.java;
import static org.junit.Assert.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** 
 * @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9
 **/
 public class Issue808{
  @SuppressWarnings("static-method") @Test public void testTerm01() {
    Expression ex = az.simpleName(wizard.ast("x"));
    assertNotEquals(ex, (new Term(true, duplicate.of(ex))).asExpression());
  }
 }