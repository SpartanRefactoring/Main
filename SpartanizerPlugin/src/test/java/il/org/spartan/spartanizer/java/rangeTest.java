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
}