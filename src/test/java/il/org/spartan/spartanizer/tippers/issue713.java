package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/**
 * see issue #713 for more details
 * @author Inbal Zukerman
 * @author Elia Traore
 * @since 2016-11-06
 */

public class issue713 {
  TypeDeclaration noPublic = (TypeDeclaration) az.compilationUnit(wizard.ast("public class noPublic {   } ")).types().get(0);
  TypeDeclaration onePublic = (TypeDeclaration) az.compilationUnit(wizard.ast("public class onePublic {  public int x; } ")).types().get(0);
  
      
  @SuppressWarnings("static-method") @Test public void doesCompile(){
    assert true;
  }
  
  @SuppressWarnings("static-method") @Test public void returnsList(){
    @SuppressWarnings("unused") List<String> lst = getAll.publicFields(null);
  }
  
  @Test public void returnsNoPublic(){
    assertEquals(0, getAll.publicFields(noPublic).size());
  }
  
  @Test public void onePublicFails(){
    assertEquals(1, getAll.publicFields(onePublic).size());
  }
  
}
