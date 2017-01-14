package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Example for using givesWithBinding(String p, String f) from class
 * {@link ExpanderTestUtils} .
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07 */
@SuppressWarnings("static-method")
@Ignore // This is only for demostration it doesn't suppose to work
public class Issue1040 {
  @Test public void test1() {
    expansionOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int a() {int i;i=0;i++;return 0;}",
        "a");
  }

  @Test public void test2() {
    expansionOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a").givesWithBinding("int b() {int i;i=0;++i;return 0;}",
        "b");
  }

  @Test public void test3() {
    expansionOf(new Issue1040Aux()).givesWithBinding("int a() {int i;i=0;++i;return 0;}", "a")
        .givesWithBinding("int b() {int i;i=0;++i;return 0;}", "b").givesWithBinding("int a() {int i;i=0;i++;return 0;}", "a")
        .givesWithBinding("int b() {int i;i=0;i++;return 0;}", "b");
  }

  @Test(expected = AssertionError.class) public void test4() {
    expansionOf(new Issue1040Aux2()).givesWithBinding("void toTest() {total = 0;for(final Integer k : arr) {total += total(1);}}", "toTest");
  }

  @Test public void test5() {
    expansionOf(new Issue1040Aux2()).givesWithBinding("void toTest2() {total2 = 0;for(final Integer k : arr) {total2 = total2 + total2(1);}}",
        "toTest2");
  }

  @Test(expected = AssertionError.class) public void test6() {
    expansionOf(new Issue1040Aux3()).givesWithBinding("void toTest() {total = 0;for(final Integer k : arr) {total += total(1);}}", "toTest");
  }

  @Test public void test7() {
    expansionOf(new Issue1040Aux3()).givesWithBinding("void toTest2() {total2 = 0;for(final Integer k : arr) {total2 = total2 + total2(1);}}",
        "toTest2");
  }

  @SuppressWarnings({ "unused" })
  public class Issue1040Aux3 extends ReflectiveTester {
    int total;
    int total2;
    @SuppressWarnings("boxing") Integer[] arr = { 1, 2, 3, 4, 5 };

    double total(final int x) {
      return 5.0;
    }

    int total2(final int x) {
      return 5;
    }

    void toTest() {
      total = 0;
      for (final Integer k : arr)
        total += total(1);
    }

    void toTest2() {
      total2 = 0;
      for (final Integer k : arr)
        total2 += total2(1);
    }
  }
}
