package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;
import il.org.spartan.spartanizer.meta.*;

/** Example for using givesWithBinding(String p, String f) from class
 * {@link BloatingTestUtilities} .
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07 */
// TODO: Yuval Simon: use fixture or inline.
@Ignore // This is only for demonstration it doesn't suppose to work
@SuppressWarnings("static-method")
public class Issue1040 {
  @Test public void test1() {
    bloatingOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int a() {int i;i=0;i++;return 0;}",
        "a");
  }

  @Test public void test2() {
    bloatingOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int b() {int i;i=0;++i;return 0;}",
        "b");
  }

  @Test public void test3() {
    bloatingOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a")
        .givesWithBinding("int b() {int i;i=0;++i;return 0;}", "b").givesWithBinding("int a() {int i;i=0;i++;return 0;}", "a")
        .givesWithBinding("int b() {int i;i=0;i++;return 0;}", "b");
  }

  @Test(expected = AssertionError.class) public void test4() {
    bloatingOf(new Issue1040Aux2()).givesWithBinding("void toTest() {total = 0;for(final Integer k : arr) {total += total(1);}}", "toTest");
  }

  @Test public void test5() {
    bloatingOf(new Issue1040Aux2()).givesWithBinding("void toTest2() {total2 = 0;for(final Integer k : arr) {total2 = total2 + total2(1);}}",
        "toTest2");
  }

  @Test(expected = AssertionError.class) public void test6() {
    bloatingOf(new Issue1040Aux3()).givesWithBinding("void toTest() {total = 0;for(final Integer k : arr) {total += total(1);}}", "toTest");
  }

  @Test public void test7() {
    bloatingOf(new Issue1040Aux3()).givesWithBinding("void toTest2() {total2 = 0;for(final Integer k : arr) {total2 = total2 + total2(1);}}",
        "toTest2");
  }

  @SuppressWarnings({ "unused" })
  public class Issue1040Aux3 extends MetaFixture {
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
      total = 0;
      total = Arrays.asList(arr).stream().map(λ -> total(1)).reduce((x, y) -> x + y).get().intValue();
    }

    @SuppressWarnings("boxing") void toTest2() {
      total2 = Arrays.asList(arr).stream().map(λ -> total2(1)).reduce((x, y) -> x + y).get();
    }
  }
}
