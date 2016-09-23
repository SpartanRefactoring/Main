package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link TernaryPushdownStrings}
 * @author Niv Shalmon
 * @since 2016-09 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue247 {
  @Test public void issue247_01() {
    trimmingOf("return basic_types.contains(base_type) ? \"<FONT COLOR=\\\"#00FF00\\\">\" + type + \"</FONT>\""
        + ": \"<A HREF=\\\"\" + base_type + \".html\\\" TARGET=_top>\" + short_type + \"</A>\";")
            .gives("return \"<\"+(basic_types.contains(base_type)?\"FONT COLOR=\\\"#00FF00\\\">\"+type+\"</FONT>\""
                + ":\"A HREF=\\\"\"+base_type+\".html\\\" TARGET=_top>\"+short_type+\"</A>\");");
  }
}
