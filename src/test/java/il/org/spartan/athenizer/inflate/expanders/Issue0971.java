package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.zoomer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Tets class for issue #971
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class Issue0971 {
  @Test public void test0() {
    expansionOf("if(true)f();").gives("if(true){f();}").stays();
  }

  @Test public void test1() {
    expansionOf("if(true)f();else g();").gives("if(true){f();}else{g();}").stays();
  }

  @Test public void test2() {
    expansionOf("if(true)f();g();").gives("if(true){f();}g();").stays();
  }

  @Test public void test3() {
    expansionOf("if(true)while(false){t();}g();").gives("if(true){while(false){t();}}g();").stays();
  }

  @Test public void test4() {
    expansionOf("if(true)t();else{g();}").gives("if(true){t();}else{g();}").stays();
  }

  @Test public void test5() {
    expansionOf("if(true){t();}else g();").gives("if(true){t();}else{g();}").stays();
  }

  @Test public void test6() {
    expansionOf("if(true)if(b==c)q();else g();").gives("if(true){if(b==c)q();else g();}")//
        .gives("if(true){if(b==c){q();}else {g();}}");
  }
}
