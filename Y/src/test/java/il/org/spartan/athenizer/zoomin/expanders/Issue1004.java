package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Test case for {@link OutlineArrayAccess}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue1004 {
  @Test public void t1() {
    bloatingOf("arr[i--] = 5;")//
        .gives("arr[i] = 5; i--;");
  }

  @Test public void t2() {
    bloatingOf("arr[i++] = i;").stays();
  }

  @Test public void t3() {
    bloatingOf("arr[i++] = arr[i++] + i;")//
        .stays();
  }

  @Test public void t4() {
    bloatingOf("arr[++i] = i;")//
        .stays();
  }

  @Test public void t5() {
    bloatingOf("arr[++i] = x;")//
        .gives("++i; arr[i] = x;");
  }

  @Test public void t6() {
    bloatingOf("arr[i++] = 5+4;")//
        .gives("arr[i] = 5+4; i++;");
  }

  @Test public void t7() {
    bloatingOf("for(;;) { arr[i++]=1;}")//
        .gives("for(;;) { arr[i]=1;i++;}");
  }

  @Test public void t8() {
    bloatingOf("for(;;) { arr[++i]=1;}")//
        .gives("for(;;) { ++i; arr[i]=1;}");
  }

  @Test public void t9() {
    bloatingOf("a[+x] = 1;")//
        .stays();
  }

  @Test public void t10() {
    bloatingOf("x = arr[i--];")//
        .gives("x = arr[i]; i--;");
  }

  @Test public void t11() {
    bloatingOf("i = arr[i];").stays();
  }

  @Test public void t12() {
    bloatingOf("f(arr[i++]);").gives("f(arr[i]);i++;");
  }

  @Test public void t13() {
    bloatingOf("f(arr[i++],arr[i++]);").stays();
  }

  @Test public void t14() {
    bloatingOf("for(;;a[i++]=1){}").stays();
  }
}
