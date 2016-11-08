package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Doron Meshulam
 * @author Tomer Dragucki */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue707 {
  @Test public void a() {
    getAll2.names(null);
  }
  @Test public void b() {
    getAll2.names((Block) null);
  }
  @Test public void c() {
    assertEquals((getAll2.names(az.block(ast("{int i;}"))).get(0) + ""), "i");
  }
  @Test public void d() {
    assertNamesList(getAll2.names(az.block(ast("{int i = x; z = 6;}"))), Arrays.asList("i", "x", "z"));
  }
  @Test public void e() {
    assertNamesList(getAll2.names(az.block(ast("{int x = i; z = 8;}"))), Arrays.asList("x", "i", "z"));
  }
  @Test public void f() {
    assertNamesList(getAll2.names(az.block(ast("{int x = i; double y = 5;}"))), Arrays.asList("x", "i", "y"));
  }
  @Test public void g() {
    assertNamesList(getAll2.names(az.block(ast("{if(i > 0)  return z;}"))), Arrays.asList("i", "z"));
  }
  static void assertNamesList(List<Name> actual, List<String> expected) {
    for (Name ¢ : actual)
      assertTrue(expected.contains((¢ + "")));
  }
  static ASTNode ast(final String ¢) {
    return wizard.ast(¢);
  }
}
