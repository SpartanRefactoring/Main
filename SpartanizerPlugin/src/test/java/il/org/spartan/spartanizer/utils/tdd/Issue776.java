package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 16-11-07 */
@SuppressWarnings("static-method")
public class Issue776 {
  @Test public void checkEmptyFunc() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{}")), 0);
  }

  @Test public void checkFuncOneBlock() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{}}")), 1);
  }

  @Test public void checkFunc2BlockRet1() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{}{}}")), 1);
  }

  @Test public void checkSeveralBlocks() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{} {} if (false) {}}")), 2);
  }

  @Test public void checkIfNoBlock() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{if (false) x=5;}")), 0);
  }

  @Test public void checkWhileAndForBlocks() {
    assertEquals(enumerate
        .blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{} {} if (false) {} while(x!=0) {} for(int i=0;i<5;i++){x=7;}}")), 4);
  }

  @Test public void checkSwitchBlocks() {
    assertEquals(enumerate.blockTypes(
        (MethodDeclaration) wizard.ast("public int foo(int x)" + "{ switch (x) {case 1:  x=7;break; default: x=9; break;} return x;}")), 1);
  }

  @Test public void checkDoBlock() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public int foo(int x)" + "{do {}while(true);}")), 1);
  }

  @Test public void checkSyncronize() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public void addName(String name)" + "{int x=0; synchronized(this) {++x;}}")),
        1);
  }

  @Test public void tryChatch() {
    assertEquals(
        enumerate.blockTypes((MethodDeclaration) wizard.ast("public void addName(String name)" + "{try {} catch (IndexOutOfBoundsException e) {}}")),
        1);
  }

  @Test public void lambadaExprCheck() {
    assertEquals(enumerate.blockTypes(
        (MethodDeclaration) wizard.ast("public void addName(String name)" + "{Runnable r = () -> {System.out.println(\"hello world\");};}")), 1);
  }

  @Test public void initArrayNotBlock() {
    assertEquals(enumerate.blockTypes((MethodDeclaration) wizard.ast("public void addName(String name)" + "{int[] a={5,2,1};}")), 0);
  }
}
