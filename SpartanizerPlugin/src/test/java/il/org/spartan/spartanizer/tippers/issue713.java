package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #713 for more details
 * @author Inbal Zukerman
 * @author Elia Traore
 * @since 2016-11-06 */
public class issue713 {
  private TypeDeclaration noPublic = (TypeDeclaration) az.compilationUnit(wizard.ast("public class noPublic {   } ")).types().get(0);
  private TypeDeclaration onlyPrivates = (TypeDeclaration) az
      .compilationUnit(wizard.ast("public class onlyPrivates { private int z,y,x; " + " private boolean aflag; } ")).types().get(0);
  private TypeDeclaration onePublic = (TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublic {  public int x; } ")).types().get(0);
  private TypeDeclaration notOnlyPublic = (TypeDeclaration) az
      .compilationUnit(wizard.ast("public class notOnlyPublic {  public int x;" + " private boolean flag; public char ch; } ")).types().get(0);
  private TypeDeclaration listOfPublicFields = (TypeDeclaration) az
      .compilationUnit(wizard.ast("public class foo {  public int x, y, z;" + " protected boolean flag; public char ch; } ")).types().get(0);
  private TypeDeclaration notCountingMethods = (TypeDeclaration) az
      .compilationUnit(wizard.ast("public class foo {  public int x, y;" + " public void func(){ int pi;} } ")).types().get(0);

  @SuppressWarnings("static-method") @Test public void doesCompile() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void returnsList() {
    getAll.publicFields(null);
  }

  @Test public void returnsNoPublic() {
    assertEquals(0, getAll.publicFields(noPublic).size());
  }

  @Test public void onePublicPass() {
    assertEquals(1, getAll.publicFields(onePublic).size());
  }

  @Test public void onlyPublicsDetected() {
    assertEquals(2, getAll.publicFields(notOnlyPublic).size());
  }

  @Test public void rightNamesReturned() {
    final List<String> names = new ArrayList<>();
    names.add("x");
    names.add("ch");
    assertEquals(names, getAll.publicFields(notOnlyPublic));
  }

  @Test public void listOfPublicFields() {
    assertEquals(4, getAll.publicFields(listOfPublicFields).size());
  }

  @Test public void notCountingMethods() {
    assertEquals(2, getAll.publicFields(notCountingMethods).size());
  }

  @Test public void listContainsRightNames() {
    final List<String> names = new ArrayList<>();
    names.add("x");
    names.add("y");
    assertEquals(names, getAll.publicFields(notCountingMethods));
  }

  @Test public void onlyPrivates() {
    assertEquals(0, getAll.publicFields(onlyPrivates).size());
  }
}
