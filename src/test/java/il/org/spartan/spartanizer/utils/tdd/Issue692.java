package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 16-11-06 */
@SuppressWarnings("static-method") //
public class Issue692 {
  @Test public void test0() {
    assertNull(getAll.invocations((MethodInvocation) null));
  }
  @Test public void test1() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,3)"))).size(), 0);
  }
  @Test public void test2() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,i)"))).size(), 1);
  }
  @Test public void test3() {
    // TODO: you may simplify the code by writing something such
    // as new TreeSet(az.list("a","b")) --yg
    final Set<String> tmp = new TreeSet<>();
    tmp.add("j");
    tmp.add("i");
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,j),i)"))), tmp);
  }
  @Test public void test4() {
    final Set<String> tmp = new TreeSet<>();
    tmp.add("j");
    tmp.add("i");
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,m(j)),i)"))), tmp);
  }
  @Test public void test5() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,m(1)),2)"))), new TreeSet<>());
  }
}