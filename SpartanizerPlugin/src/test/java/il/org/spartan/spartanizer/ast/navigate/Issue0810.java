package il.org.spartan.spartanizer.ast.navigate;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

/** Tests of {@link haz}
 * @author Netanel Felcher
 * @author Moshe Eliasof
 * @since 16-11-13 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0810 {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO_JAVA);
  final ASTNode ast = makeAST.COMPILATION_UNIT.from(f);

  @Test public void test0() {
    assert !haz.continueStatement(ast);
  }
  @Test public void test1() {
    final Collection<SimpleName> xs = an.empty.list();
    xs.add(az.simpleName(make.ast("abc")));
    assert !haz.dollar(xs);
    xs.add(az.simpleName(make.ast("$")));
    assert haz.dollar(xs);
  }
  @Test public void test2() {
    assert haz.hasNoModifiers(az.bodyDeclaration(make.ast("public void foo(){ int a=1; return;}")));
  }
  @Test public void test3() {
    assert !sideEffects.free(az.methodDeclaration(make.ast("public void foo(){ int a=1; return;}")));
  }
  @Test public void test4() {
    assert !haz.unknownNumberOfEvaluations((MethodDeclaration) make.ast("public int foo(int x){}"));
  }
  @Test public void test5() {
    assert !haz.cent(make.ast("{int a;}"));
  }
  @Test public void test6() {
    assert !haz.dollar(make.ast("{int a;}"));
    assert haz.dollar(make.ast("$"));
  }
  @Test public void test7() {
    assert haz.booleanReturnType((MethodDeclaration) make.ast("public boolean foo();"));
    assert !haz.booleanReturnType((MethodDeclaration) make.ast("public int foo();"));
  }
}