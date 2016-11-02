package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** @author Dor Ma'ayan
 * @author Omri Ben- Shmuel
 * @since 01-11-2016 */
public class Issue691 {
  @SuppressWarnings("static-method") @Test public void test0() {
    assertNull(getAll.invocations(null));
  }
  
  @SuppressWarnings("static-method") @Test public void test1() {
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {}")))).size(),0);
  }
  
  @SuppressWarnings("static-method") @Test public void test2() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a;int b;y.t(a,b);}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test3() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("g");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {t(); g();}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test4() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {t(); q();}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test5() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(); q();}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test6() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test7() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();int v = 5 +add();}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test8() {
    Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();int v = 5 +add(); int tmp = (int)5.5;}")))),res);
  }
  
  @SuppressWarnings("static-method") @Test public void test9() {
    Set<String> res = new TreeSet<>();
    res.add("getBody");
    res.add("statements");
    res.add("isEmpty");
    res.add("accept"); 
    res.add("add");
    res.add("getName");
    res.add("toString");
    assertEquals((getAll.invocations(az.methodDeclaration(wizard.ast("public static Set<String> invocations(final MethodDeclaration ¢) {"
        + "if(¢ == null)"
        + "return null;"
        + "Set<String> $ = new TreeSet<>();"
        + "if(¢.getBody().statements().isEmpty())"
        + "return  $;"
        + "¢.accept(new ASTVisitor() {"
        + "@Override public boolean visit (MethodInvocation m) {"
        + "$.add(m.getName().toString());"
        + "return true;"
        + "}"
        + "});"
        + "return $;"
        + "}")))),res);
  }
}
