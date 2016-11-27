package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;


import org.junit.*;

import il.org.spartan.*;

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
  
  
  @Test(expected=Exception.class) public void test3() {
    (new ParameterInt()).intValue();
  }
  
  @SuppressWarnings("boxing")
  @Test public void test4() {
    ParameterInt i = new ParameterInt();
    i.set(3);
    assertEquals(true, i.hasValue());
  }
  
  @SuppressWarnings("boxing")
  @Test public void test5() {
    assertEquals(false, (new ParameterInt()).hasValue());
  }
  
  @SuppressWarnings("boxing")
  @Test public void test6() {
    assertEquals(false, (new ParameterInt(2)).hasValue());
  }
}
