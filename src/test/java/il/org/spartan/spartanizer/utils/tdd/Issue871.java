package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;


import org.junit.*;

@SuppressWarnings("static-method")
public class Issue871 {
  @Test public void test0() {
    assertEquals(0, (new ParameterInt(0)).intValue());
  }
  
  @Test public void test1() {
    ParameterInt i = new ParameterInt();
    i.set(3);
    assertEquals(3, i.intValue());
  }
  
  @Test(expected=IllegalArgumentException.class) public void test2() {
    ParameterInt i = new ParameterInt();
    i.set(3);
    assertEquals(3, i.intValue());
    i.set(4);
  }
}
