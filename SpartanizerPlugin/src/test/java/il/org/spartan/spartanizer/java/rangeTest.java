package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** Test cases for the fluent API of Range
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings({ "boxing", "static-method" })
public class rangeTest {
  @Test public void test0() {
    final Int s = new Int();
    range.to(5).forEach(λ -> ++s.inner);
    assert s.inner == 5;
  }

  @Test public void test1() {
    assert range.from(3).to(5).stream().reduce((x, y) -> x + y).get() == 7;
  }

  @Test public void test10() {
    assert range.to(10).step(2).stream().map(i1 -> range.to(10).step(2).stream().reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get() == 100;
  }

  @Test public void test11() {
    assert range.to(10).step(2).stream().map(i1 -> range.to(10).step(2).inclusive().stream().reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y)
        .get() == 150;
  }

  @Test public void test2() {
    assert range.from(3).to(5).inclusive().stream().reduce((x, y) -> x + y).get() == 12;
  }

  @Test public void test3() {
    assert range.to(5).stream().count() == 5;
  }

  @Test public void test4() {
    assert range.from(3).to(5).inclusive().exclusive().stream().reduce((x, y) -> x + y).get() == 7;
  }

  @Test public void test5() {
    azzert.that(range.from(0).to(10).step(2).inclusive().stream().reduce((x, y) -> x + y).get(), is(30));
  }

  @Test public void test6() {
    azzert.that(range.to(5).exclusive().stream().reduce((x, y) -> x + y).get(), is(10));
  }

  @Test public void test7() {
    assert range.to(5).exclusive().stream().reduce((x, y) -> x + y).get() == 10;
  }

  @Test public void test8() {
    assert range.to(5).step(3).stream().reduce((x, y) -> x + y).get() == 3;
  }

  @Test public void test9() {
    assert range.from(100).to(110).step(10).stream().reduce((x, y) -> x + y).get() == 100;
  }
}
