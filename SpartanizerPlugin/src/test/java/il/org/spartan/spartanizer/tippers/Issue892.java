package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
/**
 * 
 * @author Dor Ma'ayan
 * @since 05-12-2016
 */
@SuppressWarnings("static-method")
@Ignore
public class Issue892 {
  @Test public void test0() {
    trimmingOf("b+-3").gives("b-3");
  }
  
  @Test public void test1() {
    trimmingOf("x+-3.4").gives("x-3.4");
  }
  
  @Test public void test2() {
    trimmingOf("x-+3.4").gives("x-3.4");
  }
}
