package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.is;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;

/** Tests of methods according to issue 778
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since Nov 7, 2016 */
public class Issue0778 {
  @Test @SuppressWarnings("static-method") public void test0() {
    getAll2.methods(null);
  }
  @Test @SuppressWarnings("static-method") public void test1() {
    azzert.that(ArrayList.class, is(getAll2.methods(az.compilationUnit(make.ast("public class Dog {public  void foo() {} }"))).getClass()));
  }
  @Test @SuppressWarnings("static-method") public void test2() {
    azzert.that(1, is(getAll2.methods(az.compilationUnit(make.ast("public class Dog {public  void foo() {} }"))).size()));
  }
  @Test @SuppressWarnings("static-method") public void test3() {
    azzert.that("foo",
        is(the.firstOf(getAll2.methods(az.compilationUnit(make.ast("public class Dog {public void foo() {} }")))).getName().getIdentifier()));
  }
  @Test @SuppressWarnings("static-method") public void test4() {
    azzert.that(3, is(getAll2
        .methods(az.compilationUnit(make.ast("public class Dog {public  void foo0() {} public  void foo1() {}public  void foo2() {}}"))).size()));
  }
  @Test @SuppressWarnings("static-method") public void test5() {
    azzert.that(3,
        is(getAll2
            .methods(
                az.compilationUnit(make.ast("public class Dog {public  int foo0() {return 1;} private  void foo1() {}protected  void foo2() {}}")))
            .size()));
  }
  @Test @SuppressWarnings("static-method") public void test6() {
    final List<MethodDeclaration> res = getAll
        .methods(az.compilationUnit(make.ast("public class Dog2 { public int foo0(){return 1;} private void foo1(){} protected void foo2(){}")));
    azzert.that("foo0", is(the.firstOf(res).getName().getIdentifier()));
    azzert.that("foo1", is(res.get(1).getName().getIdentifier()));
    azzert.that("foo2", is(res.get(2).getName().getIdentifier()));
  }
}
