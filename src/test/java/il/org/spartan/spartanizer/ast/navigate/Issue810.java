package il.org.spartan.spartanizer.ast.navigate;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** Tests of {@link haz}
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since 16-11-13 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue810 {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO_JAVA);
  ASTNode ast = makeAST.COMPILATION_UNIT.from(f);

  @Test public void test0() {
    assert !haz.ContinueStatement(ast);
  }

  @Test public void test1() {
    final List<SimpleName> lst = new ArrayList<>();
    lst.add(az.simpleName(wizard.ast("abc")));
    assert !haz.dollar(lst);
    lst.add(az.simpleName(wizard.ast("$")));
    assert haz.dollar(lst);
  }

  @Test public void test2() {
    assert haz.hasNoModifiers(az.bodyDeclaration(wizard.ast("public void foo(){ int a=1; return;}")));
  }

  @Test public void test3() {
    assert !haz.sideEffects(az.methodDeclaration(wizard.ast("public void foo(){ int a=1; return;}")));
  }

  @Test public void test4() {
    assert !haz.unknownNumberOfEvaluations((MethodDeclaration) wizard.ast("public int foo(int x)" + "{}"));
  }

  @Test public void test5() {
    assert !haz.cent(wizard.ast("{int a;}"));
  }

  @Test public void test6() {
    assert !haz.dollar(wizard.ast("{int a;}"));
    assert haz.dollar(wizard.ast("$"));
  }

  @Test public void test7() {
    assert haz.booleanReturnType((MethodDeclaration) wizard.ast("public boolean foo();"));
    assert !haz.booleanReturnType((MethodDeclaration) wizard.ast("public int foo();"));
  }
}