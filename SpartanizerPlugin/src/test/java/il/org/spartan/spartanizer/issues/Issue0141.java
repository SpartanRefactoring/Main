package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** unit tests for {@link ParameterAbbreviate}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0141 {
  @Test public void b$01() {
    trimmingOf("public static void go(final Object os[], final String... ss) {  \nfor (final String saa : ss) \nout(saa);  \n"
        + "out(\"elements\", os);   \n}")//
            .stays();
  }

  @Test public void b$02() {
    trimmingOf("public static void go(final List<Object> os, final String... ss) {  \nfor (final String saa : ss) \nout(saa);  \n"
        + "out(\"elements\", os);   \n}")//
            .stays();
  }

  @Test public void b$03() {
    trimmingOf("public static void go(final String ss[],String abracadabra) {  \nfor (final String a : ss) \nout(a);  \n"
        + "out(\"elements\",abracadabra);   \n}")//
            .stays();
  }

  @Test public void b$04() {
    trimmingOf("public static void go(final String ss[]) {  \nfor (final String a : ss) \nout(a);  \nout(\"elements\");   \n}").stays();
  }

  @Test public void b$05() {
    trimmingOf("public static void go(final String s[]) {  \nfor (final String a : s) \nout(a);  \nout(\"elements\");   \n}")
        .gives("public static void go(final String ss[]) {  \nfor (final String a : ss) \nout(a);  \nout(\"elements\");   \n}").stays();
  }

  @Test public void b$06() {
    trimmingOf("public static void go(final String s[][][]) {  \nfor (final String a : s) \nout(a);  \nout(\"elements\");   \n}")
        .gives("public static void go(final String ssss[][][]) {  \nfor (final String a : ssss) \nout(a);  \nout(\"elements\");   \n}").stays();
  }

  @Test public void b$07() {
    trimmingOf("public static void go(final Stringssss ssss[]) {  \nfor (final Stringssss a : ssss) \nout(a);  \nout(\"elements\");   \n}")
        .gives("public static void go(final Stringssss ss[]) {  \nfor (final Stringssss a : ss) \nout(a);  \nout(\"elements\");   \n}").stays();
  }

  @Test public void b$08() {
    trimmingOf("public static void go(final Integer ger[]) {  \nfor (final Integer a : ger) \nout(a);  \nout(\"elements\");   \n}")
        .gives("public static void go(final Integer is[]) {  \nfor (final Integer a : is) \nout(a);  \nout(\"elements\");   \n}").stays();
  }
}
