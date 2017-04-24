package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link TernaryPushdownStrings}
 * @author Niv Shalmon
 * @since 2016-09 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0247 {
  @Test public void issue247_01() {
    trimminKof("return basic_types.contains(base_type) ? \"<FONT COLOR=\\\"#00FF00\\\">\" + __ + \"</FONT>\""
        + ": \"<A HREF=\\\"\" + base_type + \".html\\\" TARGET=_top>\" + short_type + \"</A>\";")
            .gives("return \"<\"+(basic_types.contains(base_type)?\"FONT COLOR=\\\"#00FF00\\\">\"+__+\"</FONT>\""
                + ":\"A HREF=\\\"\"+base_type+\".html\\\" TARGET=_top>\"+short_type+\"</A>\");");
  }
}
