package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of {@link count.expressions}
 * @author RoeiRaz
 * @author RoeyMaor
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings("static-method")
public class Issue753 {
  @Test public void a() {
    azzert.isNull(getAll.methods(null));
  }

  /** check that the function returns an empty list if given an empty
   * Compilation unit */
  @Test public void b() {
    assert getAll.methods(az.compilationUnit(wizard.ast("public class A {}"))).isEmpty();
  }

  @Test public void c() {
    azzert.that(1, is(getAll.methods(az.compilationUnit(wizard.ast("public class A {public void foo() {}}"))).size()));
  }

  /** checking that the function returns a list of length 2 upon receiving a
   * compilation unit that contains two methods */
  @Test public void d() {
    assert getAll.methods(az.compilationUnit(wizard.ast("class A{ int func(){ return 3; } int func2(){ return 4; } }"))).size() == 2;
  }

  @Test public void e() {
    azzert.that("foo", is(getAll.methods(az.compilationUnit(wizard.ast("class A{boolean foo(){return false;}}"))).get(0).getName().getIdentifier()));
  }

  @Test public void f() {
    final String cuStr1 = "public class A " + "{void foo(/*lololo*/ ){            } }";
    final String cuStr2 = "public class B {" + " double elite(int arg1){ class InnerElite{" + " void innerfunc(){} } return 0.0; } "
        + " int anotherFunc(){} }";
    final List<MethodDeclaration> res1 = getAll.methods(az.compilationUnit(wizard.ast(cuStr1)));
    final List<MethodDeclaration> res2 = getAll.methods(az.compilationUnit(wizard.ast(cuStr2)));
    azzert.that("foo", is(res1.get(0).getName().getIdentifier()));
    azzert.that("elite", is(res2.get(0).getName().getIdentifier()));
    azzert.that("innerfunc", is(res2.get(1).getName().getIdentifier()));
    azzert.that("anotherFunc", is(res2.get(2).getName().getIdentifier()));
  }
}
