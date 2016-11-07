package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** 
 * @author Shay Segal 
 * @author Sefi Albo
 * @author Daniel Shames
 * @since 16-11-7 */
@SuppressWarnings("static-method") public class Issue681 {
  @Test public void simpleTest1() {
    assertEquals(find.ancestorMethod(null),null);
  }
  @Test public void simpleTest2() {
    ASTNode ast = wizard.ast("static void f() { return null;}");
    assertEquals(find.ancestorMethod(ast), az.methodDeclaration(ast));
  }
  @Test public void simpleTest3(){
    ASTNode funcNode = wizard.ast("static void f() { class A { }; return null;}");
    assertEquals(find.ancestorMethod(findFirst.typeDeclaration(funcNode)),az.methodDeclaration(funcNode));
  }
  @Test public void simpleTest4(){
    ASTNode funcNode = wizard.ast("static void f() { class A { int a; }; return null;}");
    assertEquals(find.ancestorMethod(findFirst.variableDeclarationFragment(findFirst.typeDeclaration(funcNode))),az.methodDeclaration(funcNode));
  }
}