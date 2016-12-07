package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since Nov 7, 2016 */
public class Issue778 {
  @Test @SuppressWarnings("static-method") public void test0() {
    getAll2.methods(null);
  }

  @Test @SuppressWarnings("static-method") public void test1() {
    azzert.that(ArrayList.class, is(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public  void foo() {} }"))).getClass()));
  }

  @Test @SuppressWarnings({ "static-method" }) public void test2() {
    azzert.that(1, is(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public  void foo() {} }"))).size()));
  }

  @Test @SuppressWarnings({ "static-method" }) public void test3() {
    azzert.that("foo",
        is(getAll2.methods(az.compilationUnit(wizard.ast("public class Dog {public void foo() {} }"))).get(0).getName().getIdentifier()));
  }

  @Test @SuppressWarnings({ "static-method" }) public void test4() {
    azzert.that(3, is(getAll2
        .methods(az.compilationUnit(wizard.ast("public class Dog {public  void foo0() {} public  void foo1() {}public  void foo2() {}}"))).size()));
  }

  @Test @SuppressWarnings({ "static-method" }) public void test5() {
    azzert
        .that(3,
            is(getAll2.methods(
                az.compilationUnit(wizard.ast("public class Dog {public  int foo0() {return 1;} private  void foo1() {}protected  void foo2() {}}")))
                .size()));
  }

  @Test @SuppressWarnings({ "static-method" }) public void test6() {
    final List<MethodDeclaration> res = getAll.methods(az.compilationUnit(
        wizard.ast("public class Dog2 {" + " public int foo0(){return 1;}" + " private void foo1(){}" + " protected void foo2(){}")));
    azzert.that("foo0", is(res.get(0).getName().getIdentifier()));
    azzert.that("foo1", is(res.get(1).getName().getIdentifier()));
    azzert.that("foo2", is(res.get(2).getName().getIdentifier()));
  }
}
