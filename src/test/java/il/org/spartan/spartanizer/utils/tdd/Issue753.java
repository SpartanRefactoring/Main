package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

import static org.junit.Assert.*;

import java.util.*;

/** Tests of {@link enumerate.expressions}
 * @author Roei-m
 * @author RoeyMaor
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //

@SuppressWarnings("static-method") public class Issue753 {
  @Test public void a() {
    assertNull(getAll.methods(null));
  }
  
  /*
   * check that the function returns an empty list if given an empty Compilation unit
   */
  @Test public void b() {
    assert(getAll.methods(az.compilationUnit(wizard.ast("public class A {}"))).isEmpty());
  }
  
  @Test public void c() {
    assertEquals(getAll.methods(az.compilationUnit(wizard.ast("public class A {public void foo() {}}"))).size(), 1);
  }
  /*
   * checking that the function returns a list of length 2
   * upon receiving a compilation unit that contains two methods 
   */
  @Test public void d() {
    assert (getAll.methods(az.compilationUnit(wizard.ast("class A{ int func(){ return 3; } int func2(){ return 4; } }"))).size() == 2);
  }
  
  @Test public void e() {
    assertEquals(getAll.methods(az.compilationUnit(wizard.ast("class A{boolean foo(){return false;}}"))).get(0).getName().getIdentifier(), "foo");
  }
  
  @Test public void f() {
    String cuStr1 = "public class A "
        + "{void foo(/*lololo*/ ){            } }";
    String cuStr2 = "public class B {"
        + " double elite(int arg1){ class InnerElite{"
        + " void innerfunc(){} } return 0.0; } "
        + " int anotherFunc(){} }";
    List<MethodDeclaration> res1 = getAll.methods(az.compilationUnit(wizard.ast(cuStr1)));
    List<MethodDeclaration> res2 = getAll.methods(az.compilationUnit(wizard.ast(cuStr2)));
    assertEquals( res1.get(0).getName().getIdentifier(), "foo");
    assertEquals( res2.get(0).getName().getIdentifier(), "elite");
    assertEquals( res2.get(1).getName().getIdentifier(), "innerfunc");
    assertEquals( res2.get(2).getName().getIdentifier(), "anotherFunc");
    
  }
  
}
