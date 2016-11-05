package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;


/** 
 * @author Alexander Kaplan
 * @author Ariel Kolikant
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc", "unused" }) //
public class Issue686 {
  @Test public void a(){
    assertNotNull(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo();"))));
  }
  
  @Test public void b(){
    assertFalse(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(String s);"))).isEmpty());
  }
  
  @Test public void c(){
    assertTrue(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(int s);"))).isEmpty());
  }
}
