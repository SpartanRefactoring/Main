package il.org.spartan.spartanizer.ast.navigate;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests of {@link haz}
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since 16-11-13 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue810 {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO_JAVA);
  ASTNode ast = makeAST.COMPILATION_UNIT.from(f);

  @Test public void test0() {
    assertFalse(haz.ContinueStatement(ast));
  }

  @Test public void test1() {
    final List<SimpleName> lst = new ArrayList<>();
    lst.add(az.simpleName(wizard.ast("abc")));
    assertFalse(haz.dollar(lst));
    lst.add(az.simpleName(wizard.ast("$")));
    assertTrue(haz.dollar(lst));
  }

  @Test public void test2() {
    assertTrue(haz.hasNoModifiers(az.bodyDeclaration(wizard.ast("public void foo(){ int a=1; return;}"))));
  }

  @Test public void test3() {
    assertFalse(haz.sideEffects(az.methodDeclaration(wizard.ast("public void foo(){ int a=1; return;}"))));
  }
}