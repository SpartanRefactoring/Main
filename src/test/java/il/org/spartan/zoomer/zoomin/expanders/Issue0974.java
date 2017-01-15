package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Test class for {@link PostFixToInfixExpander}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@Ignore // TODO Dor: when fixed please unignore the test in {@link Issue1023}.
        // they should pass
@SuppressWarnings("static-method")
public class Issue0974 {
  @Test public void test0() {
    zoomingInto("i++;")//
        .gives("i = i+1;")//
 .stays();
  }

  @Test public void test1() {
    zoomingInto("i--;")//
        .gives("i = i-1;")//
 .stays();
  }

  @Test public void test2() {
    zoomingInto("++i;")//
        .gives("i++;")//
        .gives("i = i+1;")//
 .stays();
  }

  @Test public void test3() {
    zoomingInto("--i;")//
        .gives("i--;")//
        .gives("i = i-1;")//
 .stays();
  }

  @Test public void test4() {
    zoomingInto("x = f(i--);")//
        .gives("x = f(i=i-1);")//
 .stays();
  }

  @Test public void test5() {
    zoomingInto("x = f(i++);")//
        .gives("x = f(i=i+1);")//
 .stays();
  }
}
