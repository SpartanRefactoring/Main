package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.util.*;

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
        is(the.headOf(getAll.methods(az.compilationUnit(make.ast("class A{boolean foo(){return false;}}")))).getName().getIdentifier()));
  }

  @Test public void f() {
    final List<MethodDeclaration> res2 = getAll.methods(az.compilationUnit(
        make.ast("public class B { double elite(int arg1){ class InnerElite{ void innerfunc(){} } return 0.0; }  int anotherFunc(){} }")));
    azzert.that("foo", is(
        the.headOf(getAll.methods(az.compilationUnit(make.ast("public class A {void foo(/*lololo*/ ){            } }")))).getName().getIdentifier()));
    azzert.that("elite", is(the.headOf(res2).getName().getIdentifier()));
    azzert.that("innerfunc", is(res2.get(1).getName().getIdentifier()));
    azzert.that("anotherFunc", is(res2.get(2).getName().getIdentifier()));
  }
}
