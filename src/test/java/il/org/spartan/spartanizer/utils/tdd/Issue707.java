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
    assertEquals((getAll2.names(az.block(ast("{int i;}"))).get(0) + ""), "i");
  }
  
  @Test
  public void d() {
    assertEquals((getAll2.names(az.block(ast("{int i = x;}"))).get(0) + ""), "i");
    assertEquals((getAll2.names(az.block(ast("{int i = x;}"))).get(1) + ""), "x");
  }

  @Test
  public void e() {
    assertEquals((getAll2.names(az.block(ast("{int x = i;}"))).get(0) + ""), "x");
  }
  static ASTNode ast(final String ¢) {
    return wizard.ast(¢);
  }
}
