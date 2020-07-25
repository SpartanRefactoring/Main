package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

/** Test case for {@link PrefixToPostfix}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2016-12-24 */
@SuppressWarnings("static-method")
public class Issue1005 {
  @Test public void t1() {
    bloatingOf("++i;")//
        .gives("i+=1;");
  }
  @Test public void t10() {
    bloatingOf("x = ++y;")//
        .stays();
  }
  @Test public void t11() {
    bloatingOf("int x = ++y;")//
        .gives("int x; x=++y;")//
        .stays();
  }
  @Test public void t12() {
    bloatingOf("int x = ++y + 1;")//
        .gives("int x; x=++y + 1;")//
        .stays();
  }
  @Test public void t13() {
    bloatingOf("for(;;){ --x;}")//
        .gives("for(;;){ x-=1;}");
  }
  @Test public void t14() {
    bloatingOf("--x;")//
        .gives("x-=1;");
  }
  @Test public void t15() {
    bloatingOf("for(String s=f(); !\"\".equals(s); s = f2(s)) {}")//
        .stays();
  }
  @Test public void t2() {
    bloatingOf("while(++i < 0) { x=f(i); }")//
        .stays();
  }
  @Test public void t3() {
    bloatingOf("if(++i + i++ > ++i + y) { x = f(); }")//
        .gives("if((++i+i++)>(++i+y)){x=f();}");
  }
  @Test public void t4() {
    bloatingOf("f(i++,--j,++x);")//
        .stays();
  }
  @Test public void t5() {
    bloatingOf("if(b) { ++i; }")//
        .gives("if(b) { i+=1; }");
  }
  @Test public void t6() {
    bloatingOf("for(;x<5;++x){;}")//
        .gives("for(;x<5;x+=1){;}");
  }
  @Test public void t7() {
    bloatingOf("for(++x;x<5;){;}")//
        .gives("for(x+=1;x<5;){;}");
  }
  @Test public void t9() {
    bloatingOf("for(;;) {++x;}")//
        .gives("for(;;){ x+=1;}");
  }
}
