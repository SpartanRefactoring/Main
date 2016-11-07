package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
 
/**
 * @author Doron Meshulam
 * @author Tomer Dragucki
 */

@SuppressWarnings({ "static-method", "javadoc"})
public class Issue707 {
  
  @Test
  public void a() {
    getAll2.names(null);
  }
  
  @Test
  public void b() {
    getAll2.names((Block) null);
  }
  
  @Test
  public void c() {
    assertTrue(getAll2.names(az.block(ast("{int i;}"))).contains(az.name(ast("i"))));
  }
  static ASTNode ast(final String ¢) {
    return wizard.ast(¢);
  }
}
