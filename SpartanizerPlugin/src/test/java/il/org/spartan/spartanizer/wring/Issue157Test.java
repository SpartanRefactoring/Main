package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Unit tests for Renaming Bug, Issue 157.
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method") public class Issue157Test {
  @Test public void test01(){
    trimming(" public static String combine(final Class<?>[] classes) {  \n" //
              + "final String[] ss = new String[classes.length];  \n"  //
              + "for (int i = 0; i < ss.length; ++i)  \n"  //
                + "ss[i] = classes[i] == null ? null : classes[i].getName(); \n"  //
              + "return combine(ss);  \n"  //
              + "}")
    .to(" public static String combine(final Class<?>[] cs) {  \n" //
        + "final String[] ss = new String[cs.length];  \n"  //
        + "for (int i = 0; i < ss.length; ++i)  \n"  //
          + "ss[i] = classes[i] == null ? null : cs[i].getName(); \n"  //
        + "return combine(ss);  \n"  //
        + "}");
  }
}
