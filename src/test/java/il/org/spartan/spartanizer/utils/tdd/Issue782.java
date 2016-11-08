package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author yonzarecki
 * @author rodedzats
 * @author zivizhar
 * @since Nov 8, 2016 */
public class Issue782 {
  @SuppressWarnings("static-method") @Test public void checkCompiles(){
    assert true;
  }
  
  @SuppressWarnings("static-method") @Test public void returnsNotNull(){
    assertNotNull(getAll.privateFields(null));
  }
  
  @SuppressWarnings("static-method") @Test public void emptyClassShouldReturnEmptyList(){
    assertTrue(getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class empty{}")).types().get(0))).isEmpty());
  }
  
  @SuppressWarnings("static-method") @Test public void onePrivateFieldReturnOneElementList(){
    assertEquals(1,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int x;}")).types().get(0))).size());
  }
  
  @SuppressWarnings("static-method") @Test public void onePublicFieldReturnEmptyList(){
    assertEquals(0,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublic{public int x;}")).types().get(0))).size());
  }
  
  @SuppressWarnings("static-method") @Test public void onePublicFieldAndOnePrivateFieldReturnOneElementList(){
    assertEquals(1,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublicOnePrivate{public int x; private int y;}")).types().get(0))).size());
  }
  
  @SuppressWarnings("static-method") @Test public void checkOnePrivateName(){
    assertEquals("x" ,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int x;}")).types().get(0))).get(0));
  }
  
  @SuppressWarnings("static-method") @Test public void checkAnotherPrivateName(){
    assertEquals("y" ,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int y;}")).types().get(0))).get(0));
  }
  
  @SuppressWarnings("static-method") @Test public void check2PrivatesName(){
    List<String> names = getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast(
                                              "public class twoPrivates{private int x; private int y;}")).types().get(0)));
    assertEquals("x", names.get(0));
    assertEquals("y", names.get(1));
  }
  
  @SuppressWarnings("static-method") @Test public void checkMultiDeclarationsInOneLine(){
    List<String> names = getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class onePrivate{private int a,b,c,y;}")).types().get(0)));
    assertEquals("a", names.get(0));
    assertEquals("b", names.get(1));
    assertEquals("c", names.get(2));
    assertEquals("y", names.get(3));
  }
  
}
