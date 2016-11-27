package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * tests of ParameterBool according to issue 871
 * @author kobybs
 * @since 27-11-2016
 */


@SuppressWarnings({"static-method","boxing"})
public class Issue872 {
  @Test public void test0() {
    assertEquals(false, new ParameterBool(false).boolValue());
    assertEquals(true, new ParameterBool(true).boolValue());
  }

  @Test public void test1() {
    final ParameterBool b = new ParameterBool();
    b.set(true);
    assertEquals(true, b.boolValue());
  }

  @Test(expected = IllegalArgumentException.class) public void test2() {
    final ParameterBool b = new ParameterBool();
    b.set(false);
    assertEquals(false, b.boolValue());
    b.set(true);
  }

  @Test(expected = Exception.class) public void test3() {
    new ParameterBool().boolValue();
  }

  @Test public void test4() {
    final ParameterBool b = new ParameterBool();
    b.set(true);
    assertEquals(true, b.hasValue());
  }

  @Test public void test5() {
    assertEquals(false, new ParameterBool().hasValue());
  }

  @Test public void test6() {
    assertEquals(false, new ParameterBool(true).hasValue());
  }

  @Test public void test7() {
    assertEquals(false, new ParameterBool().hasDefault());
  }

  @Test public void test8() {
    assertEquals(true, new ParameterBool(false).hasDefault());
  }
}
