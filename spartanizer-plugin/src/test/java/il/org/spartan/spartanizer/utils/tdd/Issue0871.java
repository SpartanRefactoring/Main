package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;

/** tests of ParameterInt according to issue 871
 * @author kobybs
 * @since 27-11-2016 */
@SuppressWarnings("static-method")
public class Issue0871 {
  @Test public void test0() {
    azzert.that(new ParameterInt(0).intValue(), is(0));
  }
  @Test public void test1() {
    final ParameterInt i = new ParameterInt();
    i.set(3);
    azzert.that(i.intValue(), is(3));
  }
  @Test(expected = IllegalArgumentException.class) public void test2() {
    final ParameterInt i = new ParameterInt();
    i.set(3);
    azzert.that(i.intValue(), is(3));
    i.set(4);
  }
  @Test(expected = Exception.class) public void test3() {
    new ParameterInt().intValue();
  }
  @Test public void test4() {
    final ParameterInt i = new ParameterInt();
    i.set(3);
    assert i.hasValue();
  }
  @Test public void test5() {
    assert !new ParameterInt().hasValue();
  }
  @Test public void test6() {
    assert !new ParameterInt(2).hasValue();
  }
  @Test public void test7() {
    assert !new ParameterInt().hasDefault();
  }
  @Test public void test8() {
    assert new ParameterInt(4).hasDefault();
  }
}
