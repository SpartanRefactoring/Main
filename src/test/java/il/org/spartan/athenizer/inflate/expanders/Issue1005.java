package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;
import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Test case for @link PrefixToPostfix}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */

@SuppressWarnings("static-method")
public class Issue1005 {
  @Test public void t1() {
    expandingOf("++i;").gives("i++;");
  }
  @Test public void t2() {
    expandingOf("while(++i < 0) { x=f(i); }").stays();
  }
  @Test public void t3() {
    expandingOf("if(++i + i++ > ++i + y) { x = f(); }").stays();
  }
  @Test public void t4() {
    expandingOf("f(i++,--j,++x)").stays();
  }
  @Test public void t5() {
    expandingOf("if(b) { ++i; }").gives("if(b) { i++; }");
  }
  @Test public void t6() {
    expandingOf("for(;x<5;++x);").gives("for(;x<5;x++);");
  }
  @Test public void t7() {
    expandingOf("for(++x;x<5;);").gives("for(x++;x<5;);");
  }
  @Test public void t8() {
    expandingOf("switch(++x){}").stays();
  }
  @Test public void t9() {
    expandingOf("for(;;) ++x;").gives("for(;;) x++;");
  }
  @Test public void t10() {
    expandingOf("x = ++y;").stays();
  }
  @Test public void t11() {
    expandingOf("int x = ++y;").gives("int x; x=++y;").stays();
  }
  @Test public void t12() {
    expandingOf("int x = ++y + 1;").gives("int x; x=++y + 1;").stays();
  }
  @Test public void t13() {
    expandingOf("for(;;) --x;").gives("for(;;) x--;");
  }
  @Test public void t14() {
    expandingOf("--x;").gives("x--;");
  }
}
