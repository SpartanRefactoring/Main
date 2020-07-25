package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.meta.MetaFixture;
import il.org.spartan.spartanizer.testing.TestUtilsBloating;

/** Example for using givesWithBinding(String p, String f) from class
 * {@link TestUtilsBloating} .
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-07 */
// TODO Yuval Simon: use fixture or inline.
@Ignore // This is only for demonstration it doesn't suppose to work
@SuppressWarnings("static-method")
public class Issue1040 {
  @Test public void test1() {
    bloatingOf(new Aux2()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int a() {int i;i=0;i++;return 0;}", "a");
  }
  @Test public void test2() {
    bloatingOf(new Aux2()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int b() {int i;i=0;++i;return 0;}", "b");
  }
  @Test public void test3() {
    bloatingOf(new Aux2()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int b() {int i;i=0;++i;return 0;}", "b")
        .givesWithBinding("int a() {int i;i=0;i++;return 0;}", "a").givesWithBinding("int b() {int i;i=0;i++;return 0;}", "b");
  }
  @Test(expected = AssertionError.class) public void test6() {
    bloatingOf(new Aux()).givesWithBinding("void toTest() {total = 0;for(final Integer k : arr) {total += total(1);}}", "toTest");
  }
  @Test public void test7() {
    bloatingOf(new Aux()).givesWithBinding("void toTest2() {total2 = 0;for(final Integer k : arr) {total2 = total2 + total2(1);}}", "toTest2");
  }

  @SuppressWarnings("unused")
  public static class Aux extends MetaFixture {
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

  public static class Aux2 extends MetaFixture {
    int a() {
      return 0;
    }
    int b() {
      return 0;
    }
  }
}
