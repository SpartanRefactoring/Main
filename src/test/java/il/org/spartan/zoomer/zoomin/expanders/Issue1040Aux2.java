package il.org.spartan.zoomer.zoomin.expanders;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.meta.*;

/** Example for testing with binding
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07 */
// TODO Yuval Simon --make this into an inner class
@SuppressWarnings({ "static-method", "unused" })
public class Issue1040Aux2 extends MetaFixture {
  int total;
  int total2;
  @SuppressWarnings("boxing") final Integer[] arr = { 1, 2, 3, 4, 5 };

  double total(final int x) {
    return 5.0;
  }

  int total2(final int x) {
    return 5;
  }

  @SuppressWarnings("boxing") void toTest() {
    total = Stream.of(arr).map(λ -> total(1)).reduce((x, y) -> x + y).get().intValue();
  }

  @SuppressWarnings("boxing") void toTest2() {
    total2 = Stream.of(arr).map(λ -> total2(1)).reduce((x, y) -> x + y).get();
  }
}
