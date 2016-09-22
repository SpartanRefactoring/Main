package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue141 {
  @Test public void t01() {
    trimmingOf("public static void go(final Object os[], final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n" + "out(\"elements\", os);   \n"//
        + "}").stays();
  }

  @Test public void t02() {
    trimmingOf("public static void go(final List<Object> os, final String... ss) {  \n"//
        + "for (final String saa : ss) \n"//
        + "out(saa);  \n" + "out(\"elements\", os);   \n"//
        + "}").stays();
  }

  @Test public void t03() {
    trimmingOf("public static void go(final String ss[],String abracadabra) {  \n" + "for (final String a : ss) \n" + "out(a);  \n"
        + "out(\"elements\",abracadabra);   \n" + "}").stays();
  }

  @Test public void t04() {
    trimmingOf("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void t05() {
    trimmingOf("public static void go(final String s[]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void t06() {
    trimmingOf("public static void go(final String s[][][]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ssss[][][]) {  \n" + "for (final String a : ssss) \n" + "out(a);  \n" + "out(\"elements\");   \n"
            + "}")
        .stays();
  }

  @Test public void t07() {
    trimmingOf("public static void go(final Stringssssss ssss[]) {  \n" + "for (final Stringssssss a : ssss) \n" + "out(a);  \n"
        + "out(\"elements\");   \n" + "}")
            .gives("public static void go(final Stringssssss ss[]) {  \n" + "for (final Stringssssss a : ss) \n" + "out(a);  \n"
                + "out(\"elements\");   \n" + "}")
            .stays();
  }

  @Test public void t08() {
    trimmingOf(
        "public static void go(final Integer ger[]) {  \n" + "for (final Integer a : ger) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .gives(
                "public static void go(final Integer is[]) {  \n" + "for (final Integer a : is) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .stays();
  }
}
