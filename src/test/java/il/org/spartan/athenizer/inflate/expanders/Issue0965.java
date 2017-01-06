package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Test class for issue #965
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0965 {
  @Test public void testBinding0() {
    expansionOf(new Issue965Aux())
        .givesWithBinding("package il.org.spartan.athenizer.inflate.expanders;import java.util.*;import il.org.spartan.spartanizer.ast.navigate.*;"
            + "public class Issue965Aux extends ReflectiveTester" + "{" + "  @SuppressWarnings({ \"static-method\", \"unused\" })"
            + "public void check1()" + "{" + "List<Integer> lst; lst=new ArrayList<>();" + "String s = lst+\"\";" + "}}")
        .givesWithBinding("package il.org.spartan.athenizer.inflate.expanders;import java.util.*;import il.org.spartan.spartanizer.ast.navigate.*;"
            + "public class Issue965Aux extends ReflectiveTester" + "{" + "  @SuppressWarnings({ \"static-method\", \"unused\" })"
            + "public void check1()" + "{" + "List<Integer> lst; lst=new ArrayList<>();" + "String s; s = lst+\"\";" + "}}")
        .staysWithBinding();
  }

  @Test public void testBinding1() {
    expansionOf(new Issue965Aux2()).staysWithBinding();
  }
}
