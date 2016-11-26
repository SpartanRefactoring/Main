package il.org.spartan.spartanizer.java;

import il.org.spartan.spartanizer.java.range;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings({ "boxing", "static-method" })
public class rangeTest {
  @Test public void test0() {
    range r = new range();
    int counter = 0;
    for (@SuppressWarnings("unused") Integer i : r.to(5))
      ++counter;
    assert counter == 5;
  }

  @Test public void test1() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.from(3).to(5))
      sum += ¢;
    assert sum == 7;
  }

  @Test public void test2() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.from(3).to(5).inclusive())
      sum += ¢;
    assert sum == 12;
  }

  @Test public void test3() {
    range r = new range();
    int counter = 0;
    for (@SuppressWarnings("unused") Integer i : r.to(5))
      ++counter;
    assert counter == 5;
  }

  @Test public void test4() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.from(3).to(5).inclusive().notInclusive())
      sum += ¢;
    assert sum == 7;
  }

  @Test public void test5() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.from(0).to(10).step(2).inclusive())
      sum += ¢;
    assert sum == 30;
  }

  @Test public void test6() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.to(5).inclusive())
      sum += ¢;
    assert sum == 15;
  }

  @Test public void test7() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.to(5).notInclusive())
      sum += ¢;
    assert sum == 10;
  }

  @Test public void test8() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.to(5).step(3))
      sum += ¢;
    assert sum == 3;
  }

  @Test public void test9() {
    range r = new range();
    int sum = 0;
    for (Integer ¢ : r.from(100).to(110).step(10))
      sum += ¢;
    assert sum == 100;
  }

  @Test public void test10() {
    range r1 = new range();
    range r2 = new range();
    int sum = 0;
    for (@SuppressWarnings("unused") Integer i1 : r1.to(10).step(2))
      for (Integer i2 : r2.to(10).step(2))
        sum += i2;
    assert sum == 100;
  }

  @Test public void test11() {
    range r1 = new range();
    range r2 = new range();
    int sum = 0;
    for (@SuppressWarnings("unused") Integer i1 : r1.to(10).step(2))
      for (Integer i2 : r2.to(10).step(2).inclusive())
        sum += i2;
    assert sum == 150;
  }
}