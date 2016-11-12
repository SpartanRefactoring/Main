package il.org.spartan.spartanizer.ast.navigate;
import static org.junit.Assert.*;

import java.util.*;

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
    assertEquals(3, count.statements(az.statement(wizard.ast("while(true);"))));
  }
  
  @Test public void d() {
    assertEquals(4, count.statements(az.statement(wizard.ast("if(x==5) {x++;}"))));
  }
  
  @Test public void g(){
    assertEquals(2, count.noimports(az.compilationUnit(wizard.ast("import a;"))));
  }
  @Test public void h(){
    assertEquals(3, count.noimports(az.compilationUnit(wizard.ast("package b;"))));
  }
}
