package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

//TODO: You have warnings. --yg

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 16-11-06 */
public class Issue692 {
  @SuppressWarnings("static-method") @Test public void test0() {
    assertNull(getAll.invocations((MethodInvocation) null));
  }

  @SuppressWarnings("static-method") @Test public void test1() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,3)"))).size(), 0);
  }

  @SuppressWarnings("static-method") @Test public void test2() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2),3)"))).size(), 0);
  }

  @SuppressWarnings("static-method") @Test public void test3() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(i,foo(j),3)"))).size(), 2);
  }
  @SuppressWarnings("static-method") @Test public void test4() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(i,foo(j),foo2(m,5,3,n))"))).size(), 4);
  }
  
  @SuppressWarnings("static-method") @Test public void test5() {
    final Set<String> temp = new TreeSet<>();
    temp.add("i");
    temp.add("j");
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(i,foo(j),3)"))), temp);
  }
  @SuppressWarnings("static-method") @Test public void test6() {
    final Set<String> temp = new TreeSet<>();
    temp.add("i");
    temp.add("j");
    temp.add("n");
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(i,foo(j,foo2(m(n))))"))), temp);
  }
}
