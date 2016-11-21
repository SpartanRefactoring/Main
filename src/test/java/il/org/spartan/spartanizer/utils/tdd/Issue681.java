package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames
 * @since 16-11-7 */
@SuppressWarnings("static-method")
public class Issue681 {
  @Test public void simpleTest1() {
    azzert.that(null, is(find.ancestorMethod(null)));
  }

  @Test public void simpleTest2() {
    final ASTNode ast = wizard.ast("static void f() { return null;}");
    azzert.that(az.methodDeclaration(ast), is(find.ancestorMethod(ast)));
  }

  @Test public void simpleTest3() {
    final ASTNode funcNode = wizard.ast("static void f() { class A { }; return null;}");
    azzert.that(az.methodDeclaration(funcNode), is(find.ancestorMethod(findFirst.typeDeclaration(funcNode))));
  }

  @Test public void simpleTest4() {
    final ASTNode funcNode = wizard.ast("static void f() { class A { int a; }; return null;}");
    azzert.that(az.methodDeclaration(funcNode), is(find.ancestorMethod(findFirst.variableDeclarationFragment(findFirst.typeDeclaration(funcNode)))));
  }

  @Test public void test5() {
    final ASTNode funcNode = wizard.ast("static void f() { Runnable r = (x1, x2) -> System.out.println(x1+x2); }");
    azzert.that(findFirst.methodDeclaration(funcNode),
        is(find.ancestorMethod(findFirst.infixPlus((Statement) findFirst.methodDeclaration(funcNode).getBody().statements().get(0)))));
  }
}