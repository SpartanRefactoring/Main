package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** @author Dor Ma'ayan
 * @author Omri Shmuel
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
}
