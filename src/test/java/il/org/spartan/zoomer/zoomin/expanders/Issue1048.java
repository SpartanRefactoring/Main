package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test case for bug in {@link AssignmentTernaryExpander} .
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-07 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1048 {
  @Test public void test() {
    zoomingInto(
        "public static InDeclaration instance() {" + "instance = instance != null ? instance : new InDeclaration();" + "return instance;" + "}")
            .stays();
  }
}
