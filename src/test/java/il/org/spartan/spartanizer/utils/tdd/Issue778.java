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
  @SuppressWarnings({ "static-method", "deprecation" }) @Test public void test4() {
    assertEquals(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public  void foo0() {} public  void foo1() {}public  void foo2() {}}"))).size(),3);
  }
  @SuppressWarnings({ "static-method", "deprecation" }) @Test public void test5() {
    assertEquals(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public  int foo0() {return 1;} private  void foo1() {}protected  void foo2() {}}"))).size(),3);
  }
  
  @SuppressWarnings({ "static-method", "deprecation" }) @Test public void test6() {
    
    
    String cu = "public class Dog2 {"
        + " public int foo0(){return 1;}"
        + " private void foo1(){}"
        + " protected void foo2(){}";
    List<MethodDeclaration> res = getAll.methods(az.compilationUnit(wizard.ast(cu)));
    assertEquals( res.get(0).getName().getIdentifier(), "foo0");
    assertEquals( res.get(1).getName().getIdentifier(), "foo1");
    assertEquals( res.get(2).getName().getIdentifier(), "foo2");
    
  }
  
}
