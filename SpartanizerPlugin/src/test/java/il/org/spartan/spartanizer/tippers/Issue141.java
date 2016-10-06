package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** unit tests for {@link SingleVariableDeclarationAbbreviation}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue141 {
  @Test public void b$01() {
    trimmingOf("public static void go(final Object os[], final String... ss) {  \n" + "for (final String saa : ss) \n" + "out(saa);  \n"
        + "out(\"elements\", os);   \n" + "}").stays();
  }

  @Test public void b$02() {
    trimmingOf("public static void go(final List<Object> os, final String... ss) {  \n" + "for (final String saa : ss) \n" + "out(saa);  \n"
        + "out(\"elements\", os);   \n" + "}").stays();
  }

  @Test public void b$03() {
    trimmingOf("public static void go(final String ss[],String abracadabra) {  \n" + "for (final String a : ss) \n" + "out(a);  \n"
        + "out(\"elements\",abracadabra);   \n" + "}").stays();
  }

  @Test public void b$04() {
    trimmingOf("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void b$05() {
    trimmingOf("public static void go(final String s[]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ss[]) {  \n" + "for (final String a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .stays();
  }

  @Test public void b$06() {
    trimmingOf("public static void go(final String s[][][]) {  \n" + "for (final String a : s) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final String ssss[][][]) {  \n" + "for (final String a : ssss) \n" + "out(a);  \n" + "out(\"elements\");   \n"
            + "}")
        .stays();
  }

  // TODO: Dan, ATTENTION look at this.. I saw you had some business with the relevant tipper.
  @Ignore @Test public void b$07() {
    trimmingOf("public static void go(final S ssss[]) {  \n" + "for (final S a : ssss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
        .gives("public static void go(final S ss[]) {  \n" + "for (final S a : ss) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}").stays();
  }

  @Test public void b$08() {
    trimmingOf(
        "public static void go(final Integer ger[]) {  \n" + "for (final Integer a : ger) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .gives(
                "public static void go(final Integer is[]) {  \n" + "for (final Integer a : is) \n" + "out(a);  \n" + "out(\"elements\");   \n" + "}")
            .stays();
  }
}
