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
}