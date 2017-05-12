package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;

/** tests of ParameterObject according to issue 873
 * @author kobybs
 * @since 27-11-2016 */
@SuppressWarnings("static-method")
public class Issue0873 {
  @Test public void test0() {
    azzert.that(new ParameterObject<>(new MyType(4)).objectValue().getVal(), is(4));
  }

  @Test public void test1() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(5));
    azzert.that(i.objectValue().getVal(), is(5));
  }

  @Test(expected = IllegalArgumentException.class) public void test2() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(5));
    azzert.that(i.objectValue().getVal(), is(5));
    i.set(new MyType(4));
  }

  @Test(expected = Exception.class) public void test3() {
    new ParameterObject<MyType>().objectValue();
  }

  @Test public void test4() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(3));
    assert i.hasValue();
  }

  @Test public void test5() {
    assert !new ParameterObject<MyType>().hasValue();
  }

  @Test public void test6() {
    assert !new ParameterObject<>(new MyType(2)).hasValue();
  }

  @Test public void test7() {
    assert !new ParameterObject<MyType>().hasDefault();
  }

  @Test public void test8() {
    assert new ParameterObject<>(new MyType(2)).hasDefault();
  }

  static class MyType {
    final int i;

    MyType(final int val) {
      i = val;
    }

    public int getVal() {
      return i;
    }
  }
}
