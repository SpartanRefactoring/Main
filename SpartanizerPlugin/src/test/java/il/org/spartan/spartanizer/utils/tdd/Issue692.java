package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 16-11-06 */
@SuppressWarnings("static-method") //
public class Issue692 {
  @Test public void test0() {
    azzert.isNull(getAll.invocations((MethodInvocation) null));
  }

  @Test public void test1() {
    azzert.that(0, is(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,3)"))).size()));
  }

  @Test public void test2() {
    azzert.that(1, is(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,i)"))).size()));
  }

  @Test public void test3() {
    // TODO Vivian/Ward: you may simplify the code by writing something such
    // as new TreeSet(az.list("a","b")) --yg
    final Set<String> tmp = new TreeSet<>();
    tmp.add("j");
    tmp.add("i");
    azzert.that(tmp, is(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,j),i)")))));
  }

  @Test public void test4() {
    final Set<String> tmp = new TreeSet<>();
    tmp.add("j");
    tmp.add("i");
    azzert.that(tmp, is(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,m(j)),i)")))));
  }

  @Test public void test5() {
    azzert.that(new TreeSet<>(), is(getAll.invocations(az.methodInvocation(wizard.ast("example(1,foo(2,m(1)),2)")))));
  }

  @Test public void test6() {
    final Set<String> tmp = new TreeSet<>();
    tmp.add("a");
    tmp.add("b");
    tmp.add("c");
    tmp.add("h");
    tmp.add("fizz");
    tmp.add("x");
    azzert.that(tmp, is(getAll.invocations(az.methodInvocation(wizard.ast("foo(a+b,x, y(c), 1, bar(h,j(fizz)))")))));
  }
}