package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Ignore;
import org.junit.Test;

/** TODO dormaayn: document class
 * @author dormaayn
 * @since 2017-05-23 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1392 {
  @Ignore @Test public void test0() {
    bloatingOf(" public final class Check{ public int check(){}}").gives("public final class C{public final void check(){}}");
  }
  @Test public void test1() {
    bloatingOf(" public enum Company {EBAY, PAYPAL, GOOGLE, YAHOO, ATT} Company cName;")
        .gives(" public static enum Company {EBAY, PAYPAL, GOOGLE, YAHOO, ATT} Company cName;");
  }
  @Test public void test2() {
    bloatingOf(" public static enum Company {EBAY, PAYPAL, GOOGLE, YAHOO, ATT} Company cName;").stays();
  }
}
