package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.is;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.util.measure;

/** Tests of {@link measure.expressions}
 * @author RoeiRaz
 * @author RoeyMaor
 * @since 2016 */
//
@SuppressWarnings("static-method")
public class Issue0753 {
  @Test public void a() {
    azzert.isNull(getAll.methods(null));
  }
  /** check that the function returns an empty list if given an empty
   * Compilation unit */
  @Test public void b() {
    assert getAll.methods(az.compilationUnit(make.ast("public class A {}"))).isEmpty();
  }
  @Test public void c() {
    azzert.that(1, is(getAll.methods(az.compilationUnit(make.ast("public class A {public void foo() {}}"))).size()));
  }
  /** checking that the function returns a list of length 2 upon receiving a
   * compilation unit that contains two methods */
  @Test public void d() {
    assert getAll.methods(az.compilationUnit(make.ast("class A{ int func(){ return 3; } int func2(){ return 4; } }"))).size() == 2;
  }
  @Test public void e() {
    azzert.that("foo",
        is(the.firstOf(getAll.methods(az.compilationUnit(make.ast("class A{boolean foo(){return false;}}")))).getName().getIdentifier()));
  }
  @Test public void f() {
    final List<MethodDeclaration> res2 = getAll.methods(az.compilationUnit(
        make.ast("public class B { double elite(int arg1){ class InnerElite{ void innerfunc(){} } return 0.0; }  int anotherFunc(){} }")));
    azzert.that("foo", is(the.firstOf(getAll.methods(az.compilationUnit(make.ast("public class A {void foo(/*lololo*/ ){            } }")))).getName()
        .getIdentifier()));
    azzert.that("elite", is(the.firstOf(res2).getName().getIdentifier()));
    azzert.that("innerfunc", is(res2.get(1).getName().getIdentifier()));
    azzert.that("anotherFunc", is(res2.get(2).getName().getIdentifier()));
  }
}
