package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Test case for {@link TernaryPushupStrings}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class Issue1003 {
  @Test public void t1() {
    zoomingInto("s = \"Happy \" + (holiday ? \"Hanukkah\" : \"birthday\");")//
        .gives("s = holiday ? \"Happy Hanukkah\" : \"Happy birthday\";");
  }

  @Test public void t2() {
    zoomingInto("s = (cond ? \"aa\" : \"bb\") + \"cc\";")//
        .gives("s = cond ? \"aacc\" : \"bbcc\";");
  }
}
