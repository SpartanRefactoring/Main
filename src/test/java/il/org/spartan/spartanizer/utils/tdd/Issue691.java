package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Dor Ma'ayan
 * @author Omri Ben- Shmuel
 * @since 01-11-2016 */
public class Issue691 {
  /* @ward-mattar: Added casting to solve conflict */
  @SuppressWarnings("static-method") @Test public void test0() {
    azzert.isNull(getAll.invocations((MethodDeclaration) null));
  }

  @SuppressWarnings("static-method") @Test public void test1() {
    azzert.that(0, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {}"))).size()));
  }

  @SuppressWarnings("static-method") @Test public void test2() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a;int b;y.t(a,b);}")))));
  }

  @SuppressWarnings("static-method") @Test public void test3() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("g");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {t(); g();}")))));
  }

  @SuppressWarnings("static-method") @Test public void test4() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {t(); q();}")))));
  }

  @SuppressWarnings("static-method") @Test public void test5() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(); q();}")))));
  }

  @SuppressWarnings("static-method") @Test public void test6() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();}")))));
  }

  @SuppressWarnings("static-method") @Test public void test7() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    azzert.that(res, is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();int v = 5 +add();}")))));
  }

  @SuppressWarnings("static-method") @Test public void test8() {
    final Set<String> res = new TreeSet<>();
    res.add("t");
    res.add("x");
    res.add("q");
    res.add("add");
    azzert.that(res,
        is(getAll.invocations(az.methodDeclaration(wizard.ast("static void test() {int a = t(x(),z); q();int v = 5 +add(); int tmp = (int)5.5;}")))));
  }

  @SuppressWarnings("static-method") @Test public void test9() {
    final Set<String> res = new TreeSet<>();
    res.add("getBody");
    res.add("statements");
    res.add("isEmpty");
    res.add("accept");
    res.add("add");
    res.add("getName");
    res.add("toString");
    azzert.that(res,
        is(getAll.invocations(az.methodDeclaration(wizard.ast("public static Set<String> invocations(final MethodDeclaration ¢) {" + "if(¢ == null)"
            + "return null;" + "Set<String> $ = new TreeSet<>();" + "if(¢.getBody().statements().isEmpty())" + "return  $;"
            + "¢.accept(new ASTVisitor() {" + "@Override public boolean visit (MethodInvocation m) {" + "$.add(m.getName().toString());"
            + "return true;" + "}" + "});" + "return $;" + "}")))));
  }
}
