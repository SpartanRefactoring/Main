package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.junit.*;
import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

// TODO: Add @Author, etc. --yg
// TODO: I get zillion of warnings on your code --yg
public class Issue690 {
  @Test public void test0() {
    assertNull(getAll.casts(null));
  }
  @Test public void test1() {
    assertEquals(0,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {}"))).size());
  }
  @Test public void test2() {
    assertEquals(0,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {int a;}"))).size());
  }
  @Test public void test3() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {int a = (int)2.2;}"))).size());
  }
  @Test public void test4() {
    assertEquals(2,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {int a = (int)2.2; int b = (int)3.3;}"))).size());
  }
  @Test public void test5() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {int a = (int)2.2; int b = 3;}"))).size());
  }
  @Test public void test6() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {double a = (double)2.2;}"))).size());
  }
  @Test public void test7() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static boolean foo() {if ((boolean)1==true) return true; }"))).size());
  }
  
  @Test public void test8() {
    assertEquals(0,getAll.casts(az.methodDeclaration(wizard.ast("static boolean foo() {while(1) return true; }"))).size());
  }
  
  @Test public void test9() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static boolean foo() {while((int)true==1) return true; }"))).size());
  }
  @Test public void test10() {
    assertEquals(1,getAll.casts(az.methodDeclaration(wizard.ast("static boolean foo() {while((boolean)1==true) return true; }"))).size());
  }
}
