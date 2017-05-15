package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.spartanizer.meta.*;

/** Test class for issue #965
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class Issue0965 {
  @Test public void test0() {
    bloatingOf(new TestClass()) //
        .givesWithBinding(" public String check1(){return is.toString();}", "check1")//
        .staysWithBinding();
  }

  static class TestClass extends MetaFixture {
    final List<Integer> is = an.empty.list();

    public String check1() {
      return is + "";
    }
    public void check2() {
      //
    }
    public void check3() {
      //
    }
  }
}
