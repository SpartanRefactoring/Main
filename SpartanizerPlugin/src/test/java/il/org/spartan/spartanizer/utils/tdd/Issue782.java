package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of methods according to issue 778
 * @author Yonatan Zarecki
 * @author Roded Zats
 * @author Ziv Izhar
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
  
  @SuppressWarnings("static-method") @Test public void onePrivateFieldClassShouldReturnSingleElementList(){
    assertEquals(1,getAll.privateFields(((TypeDeclaration) az.compilationUnit(wizard.ast("public class OnePrivate{private f;}")).types().get(0))));
  }
  
  
  
}
