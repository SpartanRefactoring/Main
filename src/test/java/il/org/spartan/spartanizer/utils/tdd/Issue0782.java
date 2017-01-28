package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author yonzarecki
 * @author rodedzats
 * @author zivizhar
 * @since Nov 8, 2016 */
public class Issue0782 {
  @Test @SuppressWarnings("static-method") public void check2PrivatesName() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class twoPrivates{private int x; private int y;}")))));
    azzert.that(first(names), is("x"));
    azzert.that(names.get(1), is("y"));
  }

  @Test @SuppressWarnings("static-method") public void checkAnotherPrivateName() {
    azzert.that(
        first(getAll.privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePrivate{private int y;}")))))),
        is("y"));
  }

  @Test @SuppressWarnings("static-method") public void checkCompiles() {
    assert true;
  }

  @Test @SuppressWarnings("static-method") public void checkFieldsInsideMethods() {
    final List<String> names = getAll.privateFields(
        (TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class twoPrivates{private int x; public void foo(int z){ int y; } }")))));
    azzert.that(first(names), is("x"));
    azzert.that(names.size(), is(1));
  }

  @Test @SuppressWarnings("static-method") public void checkMultiDeclarationsInOneLine() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePrivate{private int a,b,c,y;}")))));
    azzert.that(first(names), is("a"));
    azzert.that(names.get(1), is("b"));
    azzert.that(names.get(2), is("c"));
    azzert.that(names.get(3), is("y"));
  }

  @Test @SuppressWarnings("static-method") public void checkOnePrivateName() {
    azzert.that(
        first(getAll.privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePrivate{private int x;}")))))),
        is("x"));
  }

  @Test @SuppressWarnings("static-method") public void emptyClassShouldReturnEmptyList() {
    assert getAll.privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class empty{}"))))).isEmpty();
  }

  @Test @SuppressWarnings("static-method") public void onePrivateFieldReturnOneElementList() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePrivate{private int x;}"))))).size(),
        is(1));
  }

  @Test @SuppressWarnings("static-method") public void onePublicFieldAndOnePrivateFieldReturnOneElementList() {
    azzert
        .that(getAll
            .privateFields(
                (TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePublicOnePrivate{public int x; private int y;}")))))
            .size(), is(1));
  }

  @Test @SuppressWarnings("static-method") public void onePublicFieldReturnEmptyList() {
    azzert.that(getAll.privateFields((TypeDeclaration) first(types(az.compilationUnit(wizard.ast("public class onePublic{public int x;}"))))).size(),
        is(0));
  }

  @Test @SuppressWarnings("static-method") public void returnsNotNull() {
    assert getAll.privateFields(null) != null;
  }
}
