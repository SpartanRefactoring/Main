package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Check that the functions which returns all the methods invoked in a scope is
 * working well
 * @author Dor Ma'ayan
 * @author Omri Ben- Shmuel
 * @since 01-11-2016 */
@SuppressWarnings("static-method")
public class Issue0691 {
  @Test public void test0() {
    azzert.isNull(getAll.invocations((MethodDeclaration) null));
  }

  @Test public void test1() {
    azzert.that(0, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {}"))).size()));
  }

  @Test public void test2() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {int a;int b;y.t(a,b);}")))));
  }

  @Test public void test3() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("g");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {t(); g();}")))));
  }

  @Test public void test4() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {t(); q();}")))));
  }

  @Test public void test5() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {int a = t(); q();}")))));
  }

  @Test public void test6() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {int a = t(x(),z); q();}")))));
  }

  @Test public void test7() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {int a = t(x(),z); q();int v = 5 +add();}")))));
  }

  @Test public void test8() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    azzert.that(res,
        is(getAll.invocations(az.methodDeclaration(make.ast("static void test() {int a = t(x(),z); q();int v = 5 +add(); int tmp = (int)5.5;}")))));
  }

  @Test public void test9() {
    final Set<String> res = new TreeSet<>();
    res.add("body");
    res.add("statements");
    res.add("isEmpty");
    res.add("accept");
    res.add("add");
    res.add("getName");
    res.add("toString");
    azzert.that(res,
        is(getAll.invocations(az.methodDeclaration(make.ast("public static Set<String> invocations(final MethodDeclaration ¢) {if(¢ == null)"
            + "return null;Set<String> $ = new TreeSet<>();if(statements(body(¢)).isEmpty())return  $;¢.accept(new ASTVisitor(true) {"
            + "@Override public boolean visit (MethodInvocation m) {$.add(m.getName().toString());return true;}});return $;}")))));
  }
}
