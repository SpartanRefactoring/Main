package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TUnit tests for the GitHub issue thus numbered
 * @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 16-11-06 */
@SuppressWarnings("static-method") //
public class Issue0692 {
  @Test public void test0() {
    azzert.isNull(getAll.invocations((MethodInvocation) null));
  }

  @Test public void test1() {
    azzert.that(0, is(getAll.invocations(az.methodInvocation(make.ast("example(1,2,3)"))).size()));
  }

  @Test public void test2() {
    azzert.that(1, is(getAll.invocations(az.methodInvocation(make.ast("example(1,2,i)"))).size()));
  }

  final Set<String> iAndJ = new TreeSet<>(as.list("j", "i"));

  @Test public void test3() {
    azzert.that(iAndJ, is(getAll.invocations(az.methodInvocation(make.ast("example(1,foo(2,j),i)")))));
  }

  @Test public void test4() {
    azzert.that(iAndJ, is(getAll.invocations(az.methodInvocation(make.ast("example(1,foo(2,m(j)),i)")))));
  }

  @Test public void test5() {
    azzert.that(new TreeSet<>(), is(getAll.invocations(az.methodInvocation(make.ast("example(1,foo(2,m(1)),2)")))));
  }

  @Test public void test6() {
    azzert.that(new TreeSet<>(as.list("a", "b", "c", "h", "fizz", "x")),
        is(getAll.invocations(az.methodInvocation(make.ast("foo(a+b,x, y(c), 1, bar(h,j(fizz)))")))));
  }
}
