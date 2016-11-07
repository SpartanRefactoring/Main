package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;


/** 
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 16-11-7 */
@SuppressWarnings("static-method") public class Issue776 {
  @Test public void checkEmptyFunc() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{}"))), 0);
  }
  
  @Test public void checkFuncOneBlock() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{}}"))), 1);
  }
  
  @Test public void checkFunc2BlockRet1() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{}{}}"))), 1);
  }
  
  @Test public void checkSeveralBlocks() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{} {} if (false) {}}"))), 2);
  }
  
  @Test public void checkWhileAndForBlocks() {
    assertEquals(enumerate.blockTypes(((MethodDeclaration) wizard.ast("public int foo(int x)" + "{{} {} if (false) {} while(x!=0) {} for(int i=0;i<5;i++){x=7;}}"))), 4);
  }
  
  
}
