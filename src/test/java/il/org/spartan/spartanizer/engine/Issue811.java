package il.org.spartan.spartanizer.engine;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

public class Issue811 {
  /** @author Inbal Zukerman
   * @author Elia Traore
   * @since 16-11-12 */
  final MethodDeclaration nullBody = (MethodDeclaration) wizard.ast("public void f(); ");
  final MethodDeclaration emptyBody = (MethodDeclaration) wizard.ast("public void func() {} ");
  final MethodDeclaration methodWithBody = (MethodDeclaration) wizard.ast("public int function(int x) { public int count = 0; return x + count;} ");

  public class TestMethodScanner extends MethodScanner {
    public TestMethodScanner(final MethodDeclaration method) {
      super(method);
    }
    @Override protected List<Statement> availableStatements() {
      return statements;
    }
  }

  // suppress warning is needed since the c'tor receives a null param and the
  // test make sure that an actual object isn't created
  @SuppressWarnings("unused") @Test(expected = AssertionError.class) public void checkNullMethod() {
    new TestMethodScanner(null);
  }
  @Test(expected = NullPointerException.class) public void checkNullMethodBody() {
    new TestMethodScanner(nullBody).statements().iterator();
  }
  @Test public void checkEmptyMethodBody() {
    azzert.assertFalse(new TestMethodScanner(emptyBody).statements().iterator().hasNext());
  }
  @Test public void checkMethodWithBody() {
    final Iterator<Statement> iter = new TestMethodScanner(methodWithBody).statements().iterator();
    azzert.assertTrue(iter.hasNext());
    azzert.assertEquals(iter.next() + "", "public int count=0;\n");
    azzert.assertEquals(iter.next() + "", "return x + count;\n");
    azzert.assertFalse(iter.hasNext());
  }
}