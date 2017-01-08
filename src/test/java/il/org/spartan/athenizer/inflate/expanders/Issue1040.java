package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Example for using givesWithBinding(String p, String f) from class {@link ExpanderTestUtils}
 * .
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07
 */
@SuppressWarnings("static-method")
public class Issue1040 {
  @Test public void test1() {
    expansionOf(new Issue1040Aux())
    .givesWithBinding("int a() {"
        + "int i;"
        + "i=0;"
        + "++i;"
        + "return 0;"
        + "}", "a")
    .givesWithBinding("int a() {"
        + "int i;"
        + "i=0;"
        + "i++;"
        + "return 0;"
        + "}", "a");
  }
  @Test public void test2() {
    expansionOf(new Issue1040Aux())
    .givesWithBinding("int a() {"
        + "int i;"
        + "i=0;"
        + "++i;"
        + "return 0;"
        + "}", "a")
    .givesWithBinding("int b() {"
        + "int i;"
        + "i=0;"
        + "++i;"
        + "return 0;"
        + "}", "b");
  }
  @Test public void test3() {
    expansionOf(new Issue1040Aux())
    .givesWithBinding("int a() {"
        + "int i;"
        + "i=0;"
        + "++i;"
        + "return 0;"
        + "}", "a")
    .givesWithBinding("int b() {"
        + "int i;"
        + "i=0;"
        + "++i;"
        + "return 0;"
        + "}", "b")
    .givesWithBinding("int a() {"
        + "int i;"
        + "i=0;"
        + "i++;"
        + "return 0;"
        + "}", "a")
    .givesWithBinding("int b() {"
        + "int i;"
        + "i=0;"
        + "i++;"
        + "return 0;"
        + "}", "b");
  }
  @Ignore // does not pass the test, as expected it should stay.
  @Test public void test4() {
    expansionOf(new Issue1040Aux2())
      .givesWithBinding("void toTest() {"
          + "total = 0;"
          + "for(final Integer k : arr) {"
          + "total += total(1);"
          + "}"
          + "}", "toTest");
  }
  @Test public void test5() {
    expansionOf(new Issue1040Aux2())
    .givesWithBinding("void toTest2() {"
        + "total2 = 0;"
        + "for(final Integer k : arr) {"
        + "total2 = total2 + total2(1);"
        + "}"
        + "}", "toTest2");
  }
}
