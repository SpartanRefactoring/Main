package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;
import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Test case for @link PrefixToPostfix}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1005 {
  @Test public void t1() {
    expansionOf("++i;").gives("i++;");
  }

  @Test public void t2() {
    expansionOf("while(++i < 0) { x=f(i); }").stays();
  }

  @Test public void t3() {
    expansionOf("if(++i + i++ > ++i + y) { x = f(); }").stays();
  }

  @Test public void t4() {
    expansionOf("f(i++,--j,++x)").stays();
  }

  @Test public void t5() {
    expansionOf("if(b) { ++i; }").gives("if(b) { i++; }");
  }

  @Test public void t6() {
    expansionOf("for(;x<5;++x){;}").gives("for(;x<5;x++){;}");
  }

  @Test public void t7() {
    expansionOf("for(++x;x<5;){;}").gives("for(x++;x<5;){;}");
  }

  @Test public void t8() {
    expansionOf("switch(++x){}").stays();
  }

  @Test public void t9() {
    expansionOf("for(;;) {++x;}").gives("for(;;){ x++;}");
  }

  @Test public void t10() {
    expansionOf("x = ++y;").stays();
  }

  @Test public void t11() {
    expansionOf("int x = ++y;").gives("int x; x=++y;").stays();
  }

  @Test public void t12() {
    expansionOf("int x = ++y + 1;").gives("int x; x=++y + 1;").stays();
  }

  @Test public void t13() {
    expansionOf("for(;;){ --x;}").gives("for(;;){ x--;}");
  }

  @Test public void t14() {
    expansionOf("--x;").gives("x--;");
  }
}
