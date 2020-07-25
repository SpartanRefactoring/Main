package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.utils.Int;

/** Test for GitHub issue thus numbered
 * @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 * */
@SuppressWarnings("static-method")
public class Issue0801 {
  @Test public void test01() {
    azzert.that(Int.valueOf(5).inner(), is(Integer.valueOf(5)));
  }
  @Test public void test02() {
    azzert.that(Int.valueOf(0).inner(), is(Integer.valueOf(0)));
  }
}
