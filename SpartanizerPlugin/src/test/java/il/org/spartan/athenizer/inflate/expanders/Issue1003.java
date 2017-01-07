package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;
/** Test case for {@link TernaryPushupStrings}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08
 */
@SuppressWarnings("static-method")
public class Issue1003 {
  @Test public void a() {
    expansionOf("s = \"Happy \" + (holiday ? \"Hanukkah\" : \"birthday\");")
      .gives("s = holiday ? \"Happy Hanukkah\" : \"Happy birthday\";");
  }
}
