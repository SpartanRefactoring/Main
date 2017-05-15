package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Test case for {@link TernaryPushupStrings}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class Issue1003 {
  @Test public void t1() {
    bloatingOf("s = \"Happy \" + (holiday ? \"Hanukkah\" : \"birthday\");")//
        .gives("s = holiday ? \"Happy Hanukkah\" : \"Happy birthday\";");
  }
  @Test public void t2() {
    bloatingOf("s = (cond ? \"aa\" : \"bb\") + \"cc\";")//
        .gives("s = cond ? \"aacc\" : \"bbcc\";");
  }
}
