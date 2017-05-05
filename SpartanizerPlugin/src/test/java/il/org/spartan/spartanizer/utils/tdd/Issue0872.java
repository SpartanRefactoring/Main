package il.org.spartan.spartanizer.utils.tdd;

import org.junit.*;

/** tests of ParameterBool according to issue 872
 * @author kobybs
 * @since 27-11-2016 */
@SuppressWarnings("static-method")
public class Issue0872 {
  @Test public void test0() {
    final boolean generateName = new ParameterBool(false).boolValue();
    assert !generateName;
    assert new ParameterBool(true).boolValue();
  }
  @Test public void test1() {
    final ParameterBool b = new ParameterBool();
    b.set(true);
    assert b.boolValue();
  }
  @Test(expected = IllegalArgumentException.class) public void test2() {
    final ParameterBool b = new ParameterBool();
    b.set(false);
    final boolean generateName = b.boolValue();
    assert !generateName;
    b.set(true);
  }
  @Test(expected = Exception.class) public void test3() {
    new ParameterBool().boolValue();
  }
  @Test public void test4() {
    final ParameterBool b = new ParameterBool();
    b.set(true);
    assert b.hasValue();
  }
  @Test public void test5() {
    assert !new ParameterBool().hasValue();
  }
  @Test public void test6() {
    assert !new ParameterBool(true).hasValue();
  }
  @Test public void test7() {
    assert !new ParameterBool().hasDefault();
  }
  @Test public void test8() {
    assert new ParameterBool(false).hasDefault();
  }
}
