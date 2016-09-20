package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link TernaryPushdownStrings}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue236Test {
  @Test public void issue236_01() {
    trimmingOf("b ? \"a long string\" : \"another \"+\"long\"+\" string\"")//
        .gives("(b ? \"a long\" : \"another \"+\"long\"+\"\") +\" string\"")
        .gives("(b ? \"a long\" : \"another \"+\"long\") +\" string\"")
        .gives("((b ? \"a \" : \"another \"+\"\") +\"long\")+\" string\"")
        .gives("(b ? \"a \" : \"another \"+\"\") +\"long\"+\" string\"")
        .gives("(b ? \"a \" : \"another \") +\"long\"+\" string\"")
        .gives("((b ? \"a\" : \"another\") + \" \") +\"long\"+\" string\"")
        .gives("(b ? \"a\" : \"another\") + \" \" +\"long\"+\" string\"")
        .stays();
  }
  
  @Test public void issue236_02() {
    trimmingOf("b? \"something\" : \"something\"+\" else\"")
        .gives("\"something\" + (b? \"\" : \"\"+\" else\")")
        .gives("\"something\" + (b? \"\" : \" else\")").stays();
  }
  
  @Test public void issue236_03() {
    trimmingOf("isIncrement(¢) ? \"++\" : \"--\"").stays();
  }
  
  @Test public void issue236_04() {
    trimmingOf("isIncrement(¢) ? \"++x\" : \"--x\"").gives("(isIncrement(¢) ? \"++\" : \"--\")+\"x\"").stays();
  }
  
}
