package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** for more information, please view issue #690
 * @author Inbal Matityahu
 * @author Or Troyaner
 * @author Tom Nof
 * @since 16-11-04 */
@SuppressWarnings("static-method")
public class Issue0690 {
  @Test public void test0() {
    azzert.isNull(getAll.casts(null));
  }

  @Test public void test1() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {}"))).size(), is(0));
  }

  @Test public void test10() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static boolean foo() {while((boolean)1==true) return true; }"))).size(), is(1));
  }

  @Test public void test2() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {int a;}"))).size(), is(0));
  }

  @Test public void test3() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {int a = (int)2.2;}"))).size(), is(1));
  }

  @Test public void test4() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {int a = (int)2.2; int b = (int)3.3;}"))).size(), is(2));
  }

  @Test public void test5() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {int a = (int)2.2; int b = 3;}"))).size(), is(1));
  }

  @Test public void test6() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static void foo() {double a = (double)2.2;}"))).size(), is(1));
  }

  @Test public void test7() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static boolean foo() {if ((boolean)1==true) return true; }"))).size(), is(1));
  }

  @Test public void test8() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static boolean foo() {while(1) return true; }"))).size(), is(0));
  }

  @Test public void test9() {
    azzert.that(getAll.casts(az.methodDeclaration(make.ast("static boolean foo() {while((int)true==1) return true; }"))).size(), is(1));
  }
}
