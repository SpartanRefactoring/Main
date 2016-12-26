package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;
import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Test case for {@link OutlineArrayAccess}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue1004 {
  @Test public void t1() {
    expandingOf("arr[i--] = 5;").gives("arr[i] = 5; i--;");
  }

  @Test public void t2() {
    expandingOf("arr[i++] = i;").stays();
  }

  @Test public void t3() {
    expandingOf("arr[i++] = arr[i++] + i;").stays();
  }

  @Test public void t4() {
    expandingOf("arr[++i] = i;").stays();
  }

  @Test public void t5() {
    expandingOf("arr[++i] = x;").gives("++i; arr[i] = x;");
  }

  @Test public void t6() {
    expandingOf("arr[i++] = 5+4;").gives("arr[i] = 5+4; i++;");
  }

  @Test public void t7() {
    expandingOf("for(;;) { arr[i++]=1;}").gives("for(;;) { arr[i]=1;i++;}");
  }

  @Test public void t8() {
    expandingOf("for(;;) { arr[++i]=1;}").gives("for(;;) { ++i; arr[i]=1;}");
  }
}
