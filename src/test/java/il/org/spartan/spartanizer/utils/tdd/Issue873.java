package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

/** tests of ParameterObject according to issue 873
 * @author kobybs
 * @since 27-11-2016 */
@SuppressWarnings({ "static-method", "boxing" })
public class Issue873 {
  class MyType {
    int i;

    MyType(final int val) {
      i = val;
    }

    public int getVal() {
      return i;
    }
  }

  @Test public void test0() {
    assertEquals(4, new ParameterObject<>(new MyType(4)).objectValue().getVal());
  }

  @Test public void test1() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(5));
    assertEquals(5, i.objectValue().getVal());
  }

  @Test(expected = IllegalArgumentException.class) public void test2() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(5));
    assertEquals(5, i.objectValue().getVal());
    i.set(new MyType(4));
  }

  @Test(expected = Exception.class) public void test3() {
    new ParameterObject<MyType>().objectValue();
  }

  @Test public void test4() {
    final ParameterObject<MyType> i = new ParameterObject<>();
    i.set(new MyType(3));
    assertEquals(true, i.hasValue());
  }

  @Test public void test5() {
    assertEquals(false, new ParameterObject<MyType>().hasValue());
  }

  @Test public void test6() {
    assertEquals(false, new ParameterObject<>(new MyType(2)).hasValue());
  }

  @Test public void test7() {
    assertEquals(false, new ParameterObject<MyType>().hasDefault());
  }

  @Test public void test8() {
    assertEquals(true, new ParameterObject<>(new MyType(2)).hasDefault());
  }
}
