package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since Nov 7, 2016 */
public class Issue778 {
  @SuppressWarnings("static-method") @Test public void test0() {
    getAll2.methods(null);
  }
  @SuppressWarnings("static-method") @Test public void test1() {
    List<MethodDeclaration> lst = getAll2.methods(null);
  }
  @SuppressWarnings({ "static-method", "deprecation" }) @Test public void test2() {
    assertEquals(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public  void foo() {} }"))).size(), 1);
  }
  @SuppressWarnings({ "static-method", "deprecation" }) @Test public void test3() {
    assertEquals(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public void foo() {} }"))).get(0).getName().getIdentifier(), "foo");
  }
}
