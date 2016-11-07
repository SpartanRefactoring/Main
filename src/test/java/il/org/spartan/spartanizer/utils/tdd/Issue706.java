package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

// import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Unit tests for issue #706
 * @author Sapir Bismot
 * @author Yaniv Levinsky */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue706 {
  @Test public void test00() {
    getAll2.stringVariables((MethodDeclaration) null);
  }
  @Test public void test01() {
    List<VariableDeclaration> l = getAll2.stringVariables((MethodDeclaration) null);
  }
  @Test public void test02() {
    assertTrue(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("void f(int n);"))).isEmpty());
  }
  @Test public void test03() {
    assertEquals(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("void f(String s);"))).size(), 1);
  }
  @Test public void test04() {
    assertEquals(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("void f(int x, int y);"))).size(), 0);
  }
  @Test public void test05() {
    assertEquals(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("void f(int x, String s1, double y);"))).size(), 1);
  }
  @Test public void test06() {
    assertEquals(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("public String numToString(int x, String s1);"))).size(), 1);
  }
  @Test public void test07() {
    assertEquals(getAll2.stringVariables(az.methodDeclaration
        (wizard.ast("public String stringCopy(String s1, String s2, int size);"))).size(), 2);
  }
}