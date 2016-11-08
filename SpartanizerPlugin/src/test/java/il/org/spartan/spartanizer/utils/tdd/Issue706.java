package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Unit tests for issue #706
 * @author Sapir Bismot
 * @author Yaniv Levinsky */
@SuppressWarnings({ "static-method", "javadoc"}) 
public class Issue706{
  @Test public void test00() {
    getAll2.stringVariables((MethodDeclaration) null);
  }
}