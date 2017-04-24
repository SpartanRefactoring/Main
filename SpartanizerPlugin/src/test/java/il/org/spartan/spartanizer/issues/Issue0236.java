package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link TernaryPushdownStrings}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0236 {
  @Test public void issue236_01() {
    trimminKof("b ? \"a long string\" : \"another \"+\"long\"+\" string\"")//
        .gives("(b ? \"a long\" : \"another \"+\"long\"+\"\") +\" string\"")//
        .gives("(b ? \"a long\" : \"another \"+\"long\") +\" string\"")//
        .gives("((b ? \"a \" : \"another \"+\"\") +\"long\")+\" string\"")//
        .gives("(b ? \"a \" : \"another \"+\"\") +\"long\"+\" string\"")//
        .gives("(b ? \"a \" : \"another \"+\"\") +\"long string\"")//
        .gives("(b ? \"a \" : \"another \") +\"long string\"")//
        .gives("((b ? \"a\" : \"another\") + \" \") +\"long string\"")//
        .gives("(b ? \"a\" : \"another\") + \" \" +\"long string\"")//
        .gives("(b ? \"a\" : \"another\") + \" long string\"")//
        .stays();
  }

  @Test public void issue236_02() {
    trimminKof("b? \"something\" : \"something\"+\" else\"")//
        .gives("\"something\" + (b? \"\" : \"\"+\" else\")").gives("\"something\" + (b? \"\" : \" else\")")//
        .stays();
  }

  @Test public void issue236_03() {
    trimminKof("isIncrement(¢) ? \"++\" : \"--\"")//
        .stays();
  }

  @Test public void issue236_04() {
    trimminKof("isIncrement(¢) ? \"++x\" : \"--x\"")//
        .gives("(isIncrement(¢) ? \"++\" : \"--\")+\"x\"")//
        .stays();
  }
}
