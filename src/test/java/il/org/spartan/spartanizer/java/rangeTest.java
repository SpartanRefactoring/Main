package il.org.spartan.spartanizer.java;


import il.org.spartan.spartanizer.java.range;
import org.junit.*;

/** @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings("static-method")
public class rangeTest {
  @Test public void test0() {
    int counter=0;
    for(@SuppressWarnings("unused") Integer i : range.to(5))
      ++counter;
    assert counter==5;
  }
  
  @SuppressWarnings({ "static-access", "boxing" })
  @Test public void test1() {
    int sum=0;
    for(Integer ¢ : range.from(3).to(5))
      sum += ¢;
    assert sum==7;
  }
  
  @SuppressWarnings({ "static-access", "boxing" })
  @Test public void test2() {
    int sum=0;
    for(Integer ¢ : range.from(3).to(5).inclusive())
      sum += ¢;
    assert sum==12;
  }
  
  @Test public void test3() {
    int counter=0;
    for(@SuppressWarnings("unused") Integer i : range.to(5))
      ++counter;
    assert counter==5;
  }
  
  @SuppressWarnings({ "static-access", "boxing" })
  @Test public void test4() {
    int sum=0;
    for(Integer ¢ : range.from(3).to(5).inclusive().notInclusive())
      sum += ¢;
    assert sum==7;
  }
  
  @SuppressWarnings({ "static-access", "boxing" })
  @Test public void test5() {
    int sum=0;
    for(Integer ¢ : range.from(0).to(10).step(2).inclusive())
      sum += ¢;
    assert sum==30;
  }
  
  @SuppressWarnings({"boxing", "static-access" })
  @Test public void test6() {
    int sum=0;
    for(Integer ¢ : range.to(5).inclusive())
      sum += ¢;
    assert sum==15;
  }
  
  @SuppressWarnings({"boxing", "static-access" })
  @Test public void test7() {
    int sum=0;
    for(Integer ¢ : range.to(5).notInclusive())
      sum += ¢;
    assert sum==10;
  }
  
  @SuppressWarnings({"boxing", "static-access" })
  @Test public void test8() {
    int sum=0;
    for(Integer ¢ : range.to(5).step(3))
      sum += ¢;
    assert sum==3;
  }
  
  @SuppressWarnings({"boxing", "static-access" })
  @Test public void test9() {
    int sum=0;
    for(Integer ¢ : range.from(100).to(110).step(10))
      sum += ¢;
    assert sum==100;
  }
}