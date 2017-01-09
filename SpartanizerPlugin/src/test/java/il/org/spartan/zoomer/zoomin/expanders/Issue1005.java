package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

/** Test case for @link PrefixToPostfix}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
@SuppressWarnings("static-method")
public class Issue1005 {
  @Test public void t1() {
    zoomingInto("++i;").gives("i++;");
  }

  @Test public void t2() {
    zoomingInto("while(++i < 0) { x=f(i); }").stays();
  }

  @Test public void t3() {
    zoomingInto("if(++i + i++ > ++i + y) { x = f(); }").
    gives("if(++i+(i=i+1)>++i+y){x=f();}");
  }

  @Test public void t4() {
    zoomingInto("f(i++,--j,++x);").gives("f(i=i+1,--j,++x);");
  }

  @Test public void t5() {
    zoomingInto("if(b) { ++i; }").gives("if(b) { i++; }");
  }

  @Test public void t6() {
    zoomingInto("for(;x<5;++x){;}").gives("for(;x<5;x++){;}");
  }

  @Test public void t7() {
    zoomingInto("for(++x;x<5;){;}").gives("for(x++;x<5;){;}");
  }

  @Test public void t8() {
    zoomingInto("switch(++x){}").stays();
  }

  @Test public void t9() {
    zoomingInto("for(;;) {++x;}").gives("for(;;){ x++;}");
  }

  @Test public void t10() {
    zoomingInto("x = ++y;").stays();
  }

  @Ignore // see bug on #996
  @Test public void t11() {
    zoomingInto("int x = ++y;").gives("int x; x=++y;").stays();
  }

  @Ignore // see bug on #996
  @Test public void t12() {
    zoomingInto("int x = ++y + 1;").gives("int x; x=++y + 1;").stays();
  }

  @Test public void t13() {
    zoomingInto("for(;;){ --x;}").gives("for(;;){ x--;}");
  }

  @Test public void t14() {
    zoomingInto("--x;").gives("x--;");
  }

  @Test public void t15() {
    zoomingInto("for(String s=f(); !\"\".equals(s); s = f2(s)) {}").stays();
  }
}
