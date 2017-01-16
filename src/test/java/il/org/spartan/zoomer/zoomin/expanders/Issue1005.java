package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

/** Test case for @link PrefixToPostfix}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-24 */
@SuppressWarnings("static-method")
public class Issue1005 {
  @Test public void t1() {
    bloatingOf("++i;")//
        .gives("i++;");
  }

  @Test public void t2() {
    bloatingOf("while(++i < 0) { x=f(i); }")//
        .stays();
  }

  @Test public void t3() {
    bloatingOf("if(++i + i++ > ++i + y) { x = f(); }")//
        .gives("if((++i + i++) > ++i + y) { x = f(); }");
  }

  @Test public void t4() {
    bloatingOf("f(i++,--j,++x);")//
        .stays();
  }

  @Test public void t5() {
    bloatingOf("if(b) { ++i; }")//
        .gives("if(b) { i++; }");
  }

  @Test public void t6() {
    bloatingOf("for(;x<5;++x){;}")//
        .gives("for(;x<5;x++){;}");
  }

  @Test public void t7() {
    bloatingOf("for(++x;x<5;){;}")//
        .gives("for(x++;x<5;){;}");
  }

  @Test public void t8() {
    bloatingOf("switch(++x){}")//
        .stays();
  }

  @Test public void t9() {
    bloatingOf("for(;;) {++x;}")//
        .gives("for(;;){ x++;}");
  }

  @Test public void t10() {
    bloatingOf("x = ++y;")//
        .stays();
  }

  @Ignore // see bug on #996
  @Test public void t11() {
    bloatingOf("int x = ++y;")//
        .gives("int x; x=++y;")//
        .stays();
  }

  @Ignore // see bug on #996
  @Test public void t12() {
    bloatingOf("int x = ++y + 1;")//
        .gives("int x; x=++y + 1;")//
        .stays();
  }

  @Test public void t13() {
    bloatingOf("for(;;){ --x;}")//
        .gives("for(;;){ x--;}");
  }

  @Test public void t14() {
    bloatingOf("--x;")//
        .gives("x--;");
  }

  @Test public void t15() {
    bloatingOf("for(String s=f(); !\"\".equals(s); s = f2(s)) {}")//
        .stays();
  }
}
