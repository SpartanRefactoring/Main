package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 16-11-07 */
@SuppressWarnings("static-method")
public class Issue0776 {
  @Test public void checkDoBlock() {
    azzert.that(1, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){do {}while(true);}"))));
  }

  @Test public void checkEmptyFunc() {
    azzert.that(0, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){}"))));
  }

  @Test public void checkFunc2BlockRet1() {
    azzert.that(1, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){{}{}}"))));
  }

  @Test public void checkFuncOneBlock() {
    azzert.that(1, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){{}}"))));
  }

  @Test public void checkIfNoBlock() {
    azzert.that(0, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){if (false) x=5;}"))));
  }

  @Test public void checkSeveralBlocks() {
    azzert.that(2, is(enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){{} {} if (false) {}}"))));
  }

  @Test public void checkSwitchBlocks() {
    azzert.that(1, is(enumerate
        .blockTypes((MethodDeclaration) make.ast("public int foo(int x){ switch (x) {case 1:  x=7;break; default: x=9; break;} return x;}"))));
  }

  @Test public void checkSyncronize() {
    azzert.that(1, is(enumerate.blockTypes((MethodDeclaration) make.ast("public void addName(String name){int x=0; synchronized(this) {++x;}}"))));
  }

  @Test public void checkWhileAndForBlocks() {
    azzert.that(4, is(
        enumerate.blockTypes((MethodDeclaration) make.ast("public int foo(int x){{} {} if (false) {} while(x!=0) {} for(int i=0;i<5;i++){x=7;}}"))));
  }

  @Test public void initArrayNotBlock() {
    azzert.that(0, is(enumerate.blockTypes((MethodDeclaration) make.ast("public void addName(String name){int[] a={5,2,1};}"))));
  }

  @Test public void lambadaExprCheck() {
    azzert.that(1, is(enumerate
        .blockTypes((MethodDeclaration) make.ast("public void addName(String name){Runnable r = () -> {System.out.println(\"hello world\");};}"))));
  }

  @Test public void tryChatch() {
    azzert.that(1,
        is(enumerate.blockTypes((MethodDeclaration) make.ast("public void addName(String name){try {} catch (IndexOutOfBoundsException e) {}}"))));
  }
}
