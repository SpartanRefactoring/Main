package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.types;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;

/** Tests of methods according to issue 778
 * @author yonzarecki
 * @author rodedzats
 * @author zivizhar
 * @since Nov 8, 2016 */
public class Issue0782 {
  @Test @SuppressWarnings("static-method") public void check2PrivatesName() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class twoPrivates{private int x; private int y;}")))));
    azzert.that(the.firstOf(names), is("x"));
    azzert.that(names.get(1), is("y"));
  }
  @Test @SuppressWarnings("static-method") public void checkAnotherPrivateName() {
    azzert.that(
        the.firstOf(
            getAll.privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePrivate{private int y;}")))))),
        is("y"));
  }
  @Test @SuppressWarnings("static-method") public void checkCompiles() {
    assert true;
  }
  @Test @SuppressWarnings("static-method") public void checkFieldsInsideMethods() {
    final List<String> names = getAll.privateFields((TypeDeclaration) the
        .firstOf(types(az.compilationUnit(make.ast("public class twoPrivates{private int x; public void foo(int z){ int y; } }")))));
    azzert.that(the.firstOf(names), is("x"));
    azzert.that(names.size(), is(1));
  }
  @Test @SuppressWarnings("static-method") public void checkMultiDeclarationsInOneLine() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePrivate{private int a,b,c,y;}")))));
    azzert.that(the.firstOf(names), is("a"));
    azzert.that(names.get(1), is("b"));
    azzert.that(names.get(2), is("c"));
    azzert.that(names.get(3), is("y"));
  }
  @Test @SuppressWarnings("static-method") public void checkOnePrivateName() {
    azzert.that(
        the.firstOf(
            getAll.privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePrivate{private int x;}")))))),
        is("x"));
  }
  @Test @SuppressWarnings("static-method") public void emptyClassShouldReturnEmptyList() {
    assert getAll.privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class empty{}"))))).isEmpty();
  }
  @Test @SuppressWarnings("static-method") public void onePrivateFieldReturnOneElementList() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePrivate{private int x;}"))))).size(),
        is(1));
  }
  @Test @SuppressWarnings("static-method") public void onePublicFieldAndOnePrivateFieldReturnOneElementList() {
    azzert.that(getAll
        .privateFields(
            (TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePublicOnePrivate{public int x; private int y;}")))))
        .size(), is(1));
  }
  @Test @SuppressWarnings("static-method") public void onePublicFieldReturnEmptyList() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePublic{public int x;}"))))).size(),
        is(0));
  }
  @Test @SuppressWarnings("static-method") public void returnsNonNull() {
    assert getAll.privateFields(null) != null;
  }
}
