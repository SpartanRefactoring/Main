package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;
/** @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 **/
public class Issue801 {
  
  @SuppressWarnings( { "static-method", "static-access" }) @Test public void test01() {
    final Int y = new Int();
    Integer x = Integer.valueOf(5); 
        assertEquals(x, y.valueOf(5).inner() );
  }
  
  @SuppressWarnings( { "static-method", "static-access" }) @Test public void test02() {
    final Int y = new Int();
    Integer x = Integer.valueOf(0); 
        assertEquals(x, y.valueOf(0).inner() );
  }
}