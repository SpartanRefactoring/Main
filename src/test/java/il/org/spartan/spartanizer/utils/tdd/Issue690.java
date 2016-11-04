package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.junit.*;
import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

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
}
