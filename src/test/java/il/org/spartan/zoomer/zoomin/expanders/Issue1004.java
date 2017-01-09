package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Test case for {@link OutlineArrayAccess}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue1004 {
  @Test public void t1() {
    zoomingInto("arr[i--] = 5;").gives("arr[i] = 5; i--;");
  }

  @Test public void t2() {
    zoomingInto("arr[i++] = i;").stays();
  }

  @Test public void t3() {
    zoomingInto("arr[i++] = arr[i++] + i;").stays();
  }

  @Test public void t4() {
    zoomingInto("arr[++i] = i;").stays();
  }

  @Test public void t5() {
    zoomingInto("arr[++i] = x;").gives("++i; arr[i] = x;");
  }

  @Test public void t6() {
    zoomingInto("arr[i++] = 5+4;").gives("arr[i] = 5+4; i++;");
  }

  @Test public void t7() {
    zoomingInto("for(;;) { arr[i++]=1;}").gives("for(;;) { arr[i]=1;i++;}");
  }

  @Test public void t8() {
    zoomingInto("for(;;) { arr[++i]=1;}").gives("for(;;) { ++i; arr[i]=1;}");
  }

  @Test public void t9() {
    zoomingInto("a[+x] = 1;").stays();
  }
}
