package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Tets class for {@link IfElseBlockExpander}
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class Issue0971 {
  @Test public void test0() {
    bloatingOf("if(true)f();")//
        .gives("if(true){f();}")//
        .stays();
  }

  @Test public void test1() {
    bloatingOf("if(true)f();else g();")//
        .gives("if(true){f();}else{g();}")//
        .stays();
  }

  @Test public void test2() {
    bloatingOf("if(true)f();g();")//
        .gives("if(true){f();}g();")//
        .stays();
  }

  @Test public void test3() {
    bloatingOf("if(true)while(false){t();}g();")//
        .gives("if(true){while(false){t();}}g();")//
        .stays();
  }

  @Test public void test4() {
    bloatingOf("if(true)t();else{g();}")//
        .gives("if(true){t();}else{g();}")//
        .stays();
  }

  @Test public void test5() {
    bloatingOf("if(true){t();}else g();")//
        .gives("if(true){t();}else{g();}")//
        .stays();
  }

  @Test public void test6() {
    bloatingOf("if(true)if(b==c)q();else g();")//
        .gives("if(true){if(b==c)q();else g();}")//
        .gives("if(true){if(b==c){q();}else {g();}}");
  }
}
