package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;

/**
 * @author Matteo Orru'
 * @since 2016 */
public class Issue408 {
  
//  @Ignore("addition zero") 
  @Test public void issue408_01() {
    trimmingOf("0+x").gives("x").stays();
  }
  
  @Ignore @Test public void issue408_02() {
    trimmingOf("0+(0+x+y+(4))").gives("x+y+4").stays();
  }
  
//  @Ignore("addition zero") 
  @Test 
  public void issue408_03() {
    trimmingOf("0+x+y+4").gives("x+y+4").stays();
  }
  
  @Ignore("addition zero") 
  @Test public void issue408_04() {
    trimmingOf("x+0").gives("x").stays();
  }

  @Ignore("addition zero") @Test public void issue408_05() {
    trimmingOf("0+x+3").gives("x+3").stays();
  }
  
  @Ignore("addition zero") @Test public void issue408_06() {
    trimmingOf("x+0+y").gives("x+y").stays();
  }
  
  // taking this test here temporary
  @Ignore @Test public void issue408_07() {
    trimmingOf("0+(0+x+y+(4))").gives("x+y+4").stays();
  }

}
