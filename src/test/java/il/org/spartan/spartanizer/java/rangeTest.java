package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings({ "boxing", "static-method" })
public class rangeTest {
  @Test public void test0() {
    final Int s = new Int();
    range.to(5).forEach(i -> ++s.inner);
    assert s.inner == 5;
  }

  @Test public void test1() {
    assert range.from(3).to(5).stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 7;
  }

  @Test public void test10() {
    int sum = 0;
    for (@SuppressWarnings("unused") final Integer i1 : range.to(10).step(2))
      for (final Integer i2 : range.to(10).step(2))
        sum += i2;
    assert sum == 100;
  }

  @Test public void test11() {
    int sum = 0;
    for (@SuppressWarnings("unused") final Integer i1 : range.to(10).step(2))
      for (final Integer i2 : range.to(10).step(2).inclusive())
        sum += i2;
    assert sum == 150;
  }

  @Test public void test2() {
    assert range.from(3).to(5).inclusive().stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 12;
  }

  @Test public void test3() {
    int counter = 0;
    for (@SuppressWarnings("unused") final Integer i : range.to(5))
      ++counter;
    assert counter == 5;
  }

  @Test public void test4() {
    assert range.from(3).to(5).inclusive().exclusive().stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 7;
  }

  @Test public void test5() {
    azzert.that(range.from(0).to(10).step(2).inclusive().stream().map(¢ -> ¢).reduce((x, y) -> x + y).get(), is(30));
  }

  @Test public void test6() {
    azzert.that(range.to(5).exclusive().stream().map(¢ -> ¢).reduce((x, y) -> x + y).get(), is(10));
  }

  @Test public void test7() {
    assert range.to(5).exclusive().stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 10;
  }

  @Test public void test8() {
    assert range.to(5).step(3).stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 3;
  }

  @Test public void test9() {
    assert range.from(100).to(110).step(10).stream().map(¢ -> ¢).reduce((x, y) -> x + y).get() == 100;
  }
}
