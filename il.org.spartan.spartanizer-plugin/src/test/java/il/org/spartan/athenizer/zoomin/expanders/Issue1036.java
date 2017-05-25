package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.spartanizer.meta.*;

/** Test class for issue #1036
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-01-12 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1036 {
  @Test public void test0() {
    bloatingOf(new TestClass()) //
        .givesWithBinding(
            "@SuppressWarnings(\"null\")public String check1(){for(int i=1;i<children1.size();i++){final String diff=null;$=!Objects.equals($,\"\")||diff==null?$:diff;if(!$.equals(diff)&&!\"\".equals(diff))return null;}return \"\";}",
            "check1");
  }

  static class TestClass extends MetaFixture {
    final List<Integer> children1 = an.empty.list();
    List<Integer> children2 = an.empty.list();
    String $ = "";

    public String check1() {
      return "";
    }
  }
}
