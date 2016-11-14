package il.org.spartan.spartanizer.ast.engine;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/**
 * 
 * @author Dor Ma'ayan
 * @since 14-11-2016
 */
@SuppressWarnings("static-method")
public class Issue_836 {
  @Test public void test0() {
    assertEquals(2,az.block(wizard.ast("{int a;return a;}")).statements().size());
  }
  
  @Test public void test1() {
    assertEquals(0,az.block(wizard.ast("{}")).statements().size());
   }
  
  @Test public void test2() {
    assertEquals(1,az.block(wizard.ast("{{int a;}}")).statements().size());
   }
  
  @Test public void test3() {
    assertEquals(2,az.block(wizard.ast("{if(a==4){int a;}return true;}")).statements().size());
   }
  
  @Test public void test4() {
    assertEquals(1,az.block(wizard.ast("{return 1;}")).statements().size());
   }
  
}
