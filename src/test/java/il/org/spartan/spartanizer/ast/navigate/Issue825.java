package il.org.spartan.spartanizer.ast.navigate;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/**
 * test for count.java
 * @author KaplanAlexander
 * @author Kolikant
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc", "unused" }) 
public class Issue825 {
  @Test public void a() {
    assertEquals(1, count.imports(az.compilationUnit(wizard.ast("import java.util.*;"))));
  }
  
  @Test public void b() {
    assertEquals(2, count.imports(az.compilationUnit(wizard.ast("import java.util.*; import il.org.spartan.spartanizer.ast.navigate.*;"))));
  }
  
  @Test public void c() {
    assertEquals(16, count.lines(wizard.ast("String y; int x=1; int z=2; z=x; x=3;")));
  }
  
  @Test public void e() {
    count.nodesOfClass(wizard.ast("String y; int x=1; int z=2; z=x; x=3;"), wizard.ast("String y; int x=1; int z=2; z=x; x=3;").getClass());
  }
  
}
