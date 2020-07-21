package il.org.spartan.spartanizer.java;

import org.junit.*;

import fluent.ly.*;

/** TODO Sapir Bismot please add a description
 * @author Sapir Bismot
 * @since 04-12-2016 */
@SuppressWarnings("static-method")
public class infiniteRangeTest {
  @Test public void test1() {
    int x = 0;
    for (@SuppressWarnings("unused") final Integer i : range.infinite(0)) {
      ++x;
      if (x == 200)
        break;
    }
    assert x == 200;
  }
  @Test public void test2() {
    int x = 12;
    for (final Integer ¢ : range.infinite().from(12)) {
      assert ¢.intValue() == x;
      ++x;
      if (x == 200)
        break;
    }
    assert x == 200;
  }
  @Test public void test3() {
    int x = 12;
    for (final Integer ¢ : range.infinite().from(12).step(3)) {
      assert ¢.intValue() == x;
      x += 3;
      if (x > 200)
        break;
    }
    assert x == 201;
  }
  @Test public void test4() {
    int x = 12;
    for (final Integer ¢ : range.from(12).step(3).infinite()) {
      assert ¢.intValue() == x;
      x += 3;
      if (x > 200)
        break;
    }
    assert x == 201;
  }
  @Test public void test5() {
    int x = 12;
    for (final Integer ¢ : range.from(12).infinite()) {
      assert ¢.intValue() == x;
      ++x;
      if (x > 200)
        break;
    }
    assert x == 201;
  }
  @Test public void test6() {
    int x = 12;
    for (final Integer ¢ : range.from(12).infinite().step(4)) {
      assert ¢.intValue() == x;
      x += 4;
      if (x == 200)
        break;
    }
    assert x == 200;
  }
}
