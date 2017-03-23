package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.spartanizer.meta.*;

/** TODO dormaayn: test class for {@link PlusAssignToPostfix}
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-23 [[SuppressWarningsSpartan]] */
@Ignore
@SuppressWarnings("static-method")
public class Issue1138 {
  @Test public void test0() {
    bloatingOf(Issue1138Aux.instance()).givesWithBinding("void f1() {" + "a=0;" + "a++;" + "}", "f1");
  }

  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings({ "TooBroadScope" })
  public static class Issue1138Aux extends MetaFixture {
    static int a;

    @NotNull public static Issue1138Aux instance() {
      return new Issue1138Aux();
    }

    void f1() {
      a = 0;
      a += 1;
    }
  }
}
