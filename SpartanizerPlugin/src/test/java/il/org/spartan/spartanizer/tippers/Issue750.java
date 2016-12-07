package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

/**
 * 
 * @author doron
 *
 */

// TODO: Doron Meshulam - please add @since
// TODO: Doron Meshulam - You have warnings on this class. 
//TODO: Doron Meshulam - Please use full name.
public class Issue750 {
  
  @Test public void testDidntSpoiledAnything() {
    trimmingOf("void f (int x) {for(Object o : x) { System.out.println(o);}}")
    .gives("void f (int x) {for(Object ¢ : x) { System.out.println(¢);}}");
  }
  
  @Test public void testNoChange() {
    trimmingOf("void f (int ¢) {for(Object o : ¢) System.out.println(o);}").stays();
  }
}