package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

public class Issue687 {
  private static void forceTypeAtCompileTime(List<Name> ss) {
    assert true; 
   }
  @Test public void testGetNull() {
    assertNull(getAll.names(null));

  }

  @Test public void testReturnType() {
    forceTypeAtCompileTime(getAll.names((Block) wizard.ast("{}")));
  }
  
  @Test public void testGetEmpty() {
    assertTrue(getAll.names((Block) wizard.ast("{}")).isEmpty());
  }
  
  @Test public void testGetOneNameSize() {
    assertTrue(getAll.names((Block) wizard.ast("{a=1+1;}")).size()==1);
  }
  
  @Test public void testGetTwoNamesSize() {
    assertTrue(getAll.names((Block) wizard.ast("{a=1+1;b=2+2;}")).size()==2);
  }
  
  @Test public void testCheckActualName() {
    assertTrue(getAll.names((Block) wizard.ast("{a=1+1;}")).get(0).toString().equals("a"));
  }
  
  
}
