package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Test class for issue #1036
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-01-12 */
public class Issue1036 {
  @Test public void test0() {
    expansionOf(new TestClass()) //
        .givesWithBinding("@SuppressWarnings(\"null\")public String check1(){" + "for(int i=1;i<children1.size();i++){"
            + "final String diff;diff=null;if($!=\"\"||diff==null)"
            + "$=$;else $=diff;if(!$.equals(diff)&&!\"\".equals(diff))return null;}return \"\";}", "check1");
  }

  class TestClass extends ReflectiveTester {
    List<Integer> children1 = new ArrayList<>();
    List<Integer> children2 = new ArrayList<>();
    String $ = "";

    /**  */
    @SuppressWarnings("null") public String check1() {
      for (int i = 1; i < children1.size(); ++i) {
        final String diff = null;
        $ = $ != "" || diff == null ? $ : diff;
        if (!$.equals(diff) && !"".equals(diff))
          return null;
      }
      return "";
    }
  }
}
