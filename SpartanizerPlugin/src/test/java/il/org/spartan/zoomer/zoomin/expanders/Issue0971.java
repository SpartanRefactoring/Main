package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Tets class for issue #971
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class Issue0971 {
  @Test public void test0() {
    zoomingInto("if(true)f();").gives("if(true){f();}").stays();
  }

  @Test public void test1() {
    zoomingInto("if(true)f();else g();").gives("if(true){f();}else{g();}").stays();
  }

  @Test public void test2() {
    zoomingInto("if(true)f();g();").gives("if(true){f();}g();").stays();
  }

  @Test public void test3() {
    zoomingInto("if(true)while(false){t();}g();").gives("if(true){while(false){t();}}g();").stays();
  }

  @Test public void test4() {
    zoomingInto("if(true)t();else{g();}").gives("if(true){t();}else{g();}").stays();
  }

  @Test public void test5() {
    zoomingInto("if(true){t();}else g();").gives("if(true){t();}else{g();}").stays();
  }

  @Test public void test6() {
    zoomingInto("if(true)if(b==c)q();else g();").gives("if(true){if(b==c)q();else g();}")//
        .gives("if(true){if(b==c){q();}else {g();}}");
  }
}
