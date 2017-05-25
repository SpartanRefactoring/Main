package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

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
