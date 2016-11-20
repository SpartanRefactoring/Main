package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author yonzarecki
 * @author rodedzats
 * @author zivizhar
 * @since Nov 8, 2016 */
public class Issue782 {
  @SuppressWarnings("static-method") @Test public void checkCompiles() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void returnsNotNull() {
    assert getAll.privateFields(null) != null;
  }

  @SuppressWarnings("static-method") @Test public void emptyClassShouldReturnEmptyList() {
    assert getAll.privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class empty{}")).types().get(0)).isEmpty();
  }

  @SuppressWarnings("static-method") @Test public void onePrivateFieldReturnOneElementList() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int x;}")).types().get(0)).size(),
        is(1));
  }

  @SuppressWarnings("static-method") @Test public void onePublicFieldReturnEmptyList() {
    azzert.that(getAll.privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublic{public int x;}")).types().get(0)).size(),
        is(0));
  }

  @SuppressWarnings("static-method") @Test public void onePublicFieldAndOnePrivateFieldReturnOneElementList() {
    azzert
        .that(getAll
            .privateFields(
                (TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublicOnePrivate{public int x; private int y;}")).types().get(0))
            .size(), is(1));
  }

  @SuppressWarnings("static-method") @Test public void checkOnePrivateName() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int x;}")).types().get(0)).get(0),
        is("x"));
  }

  @SuppressWarnings("static-method") @Test public void checkAnotherPrivateName() {
    azzert.that(
        getAll.privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int y;}")).types().get(0)).get(0),
        is("y"));
  }

  @SuppressWarnings("static-method") @Test public void check2PrivatesName() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class twoPrivates{private int x; private int y;}")).types().get(0));
    azzert.that(names.get(0), is("x"));
    azzert.that(names.get(1), is("y"));
  }

  @SuppressWarnings("static-method") @Test public void checkMultiDeclarationsInOneLine() {
    final List<String> names = getAll
        .privateFields((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int a,b,c,y;}")).types().get(0));
    azzert.that(names.get(0), is("a"));
    azzert.that(names.get(1), is("b"));
    azzert.that(names.get(2), is("c"));
    azzert.that(names.get(3), is("y"));
  }

  @SuppressWarnings("static-method") @Test public void checkFieldsInsideMethods() {
    final List<String> names = getAll.privateFields((TypeDeclaration) az
        .compilationUnit(wizard.ast("public class twoPrivates{private int x; public void foo(int z){ int y; } }")).types().get(0));
    azzert.that(names.get(0), is("x"));
    azzert.that(names.size(), is(1));
  }
}
