package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.types;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.utils.tdd.getAll;

/** see issue #713 for more details
 * @author Inbal Zukerman
 * @author Elia Traore
 * @since 2016-11-06 */
public class issue0713 {
  final TypeDeclaration noPublic = (TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class noPublic {   } "))));
  final TypeDeclaration onlyPrivates = (TypeDeclaration) the
      .firstOf(types(az.compilationUnit(make.ast("public class onlyPrivates { private int z,y,x;  private boolean aflag; } "))));
  final TypeDeclaration onePublic = (TypeDeclaration) the.firstOf(types(az.compilationUnit(make.ast("public class onePublic {  public int x; } "))));
  final TypeDeclaration notOnlyPublic = (TypeDeclaration) the
      .firstOf(types(az.compilationUnit(make.ast("public class notOnlyPublic {  public int x; private boolean flag; public char ch; } "))));
  final TypeDeclaration listOfPublicFields = (TypeDeclaration) the
      .firstOf(types(az.compilationUnit(make.ast("public class foo {  public int x, y, z; protected boolean flag; public char ch; } "))));
  final TypeDeclaration notCountingMethods = (TypeDeclaration) the
      .firstOf(types(az.compilationUnit(make.ast("public class foo {  public int x, y; public void func(){ int pi;} } "))));

  @Test @SuppressWarnings("static-method") public void doesCompile() {
    assert true;
  }
  @Test public void listContainsRightNames() {
    final List<String> names = an.empty.list();
    names.add("x");
    names.add("y");
    azzert.that(getAll.publicFields(notCountingMethods), is(names));
  }
  @Test public void listOfPublicFields() {
    azzert.that(getAll.publicFields(listOfPublicFields).size(), is(4));
  }
  @Test public void notCountingMethods() {
    azzert.that(getAll.publicFields(notCountingMethods).size(), is(2));
  }
  @Test public void onePublicPass() {
    azzert.that(getAll.publicFields(onePublic).size(), is(1));
  }
  @Test public void onlyPrivates() {
    azzert.that(getAll.publicFields(onlyPrivates).size(), is(0));
  }
  @Test public void onlyPublicsDetected() {
    azzert.that(getAll.publicFields(notOnlyPublic).size(), is(2));
  }
  @Test @SuppressWarnings("static-method") public void returnsList() {
    getAll.publicFields(null);
  }
  @Test public void returnsNoPublic() {
    azzert.that(getAll.publicFields(noPublic).size(), is(0));
  }
  @Test public void rightNamesReturned() {
    final List<String> names = an.empty.list();
    names.add("x");
    names.add("ch");
    azzert.that(getAll.publicFields(notOnlyPublic), is(names));
  }
}
