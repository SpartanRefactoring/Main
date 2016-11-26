package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.java.range;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings({ "boxing", "static-method" })
public class rangeTest {
  @Test public void test0() {
    int s = 0;
    for (@SuppressWarnings("unused") Integer i : range.to(5))
      ++s;
    assert s == 5;
  }

  @Test public void test1() {
    int sum = 0;
    for (Integer ¢ : range.from(3).to(5))
      sum += ¢;
    assert sum == 7;
  }

  @Test public void test2() {
    int sum = 0;
    for (Integer ¢ : range.from(3).to(5).inclusive())
      sum += ¢;
    assert sum == 12;
  }

  @Test public void test3() {
    int counter = 0;
    for (@SuppressWarnings("unused") Integer i : range.to(5))
      ++counter;
    assert counter == 5;
  }

  @Test public void test4() {
    int sum = 0;
    for (Integer ¢ : range.from(3).to(5).inclusive().exclusive())
      sum += ¢;
    assert sum == 7;
  }

  @Test public void test5() {
    int sum = 0;
    for (Integer ¢ : range.from(0).to(10).step(2).inclusive())
      sum += ¢;
    azzert.that(sum, is(30));
  }

  @Test public void test6() {
    int sum = 0;
    for (Integer ¢ : range.to(5).exclusive())
      sum += ¢;
    azzert.that(sum, is(10));
  }

  @Test public void test7() {
    int sum = 0;
    for (Integer ¢ : range.to(5).exclusive())
      sum += ¢;
    assert sum == 10;
  }

  @Test public void test8() {
    int sum = 0;
    for (Integer ¢ : range.to(5).step(3))
      sum += ¢;
    assert sum == 3;
  }

  @Test public void test9() {
    int sum = 0;
    for (Integer ¢ : range.from(100).to(110).step(10))
      sum += ¢;
    assert sum == 100;
  }

  @Test public void test10() {
    int sum = 0;
    for (@SuppressWarnings("unused") Integer i1 : range.to(10).step(2))
      for (Integer i2 : range.to(10).step(2))
        sum += i2;
    assert sum == 100;
  }

  @Test public void test11() {
    int sum = 0;
    for (@SuppressWarnings("unused") Integer i1 : range.to(10).step(2))
      for (Integer i2 : range.to(10).step(2).inclusive())
        sum += i2;
    assert sum == 150;
  }
}