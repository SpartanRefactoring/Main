package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Test class for {@link PostFixToInfixExpander}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class Issue0974 {
  @Test public void test0() {
    bloatingOf("i++;")//
        .stays();
  }

  @Test public void test1() {
    bloatingOf("i--;")//
        .stays();
  }

  @Test public void test2() {
    bloatingOf("++i;")//
        .gives("i+=1;")//
        .stays();
  }

  @Test public void test3() {
    bloatingOf("--i;")//
        .gives("i-=1;")//
        .stays();
  }

  @Test public void test4() {
    bloatingOf("x = f(i--);")//
        .stays();
  }

  @Test public void test5() {
    bloatingOf("x = f(i++);")//
        .stays();
  }
}
