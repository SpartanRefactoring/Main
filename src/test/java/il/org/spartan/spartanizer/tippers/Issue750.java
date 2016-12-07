package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/**
 * @since 06-Dec-16
 * @author Doron Meshulam
 *
 */
public class Issue750 {
  
  @Test public static void testDidntSpoiledAnything() {
    trimmingOf("void f (int x) {for(Object o : x) { System.out.println(o);}}")
    .gives("void f (int x) {for(Object ¢ : x) { System.out.println(¢);}}");
  }
  
  @Test public static void testNoChange() {
    trimmingOf("void f (int ¢) {for(Object o : ¢) System.out.println(o);}").stays();
  }
}