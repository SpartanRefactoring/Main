package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for Renaming fault, Issue0 157.
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method")
public final class Issue0157 {
  @Test public void test01() {
    trimmingOf("public static String combine(final Cla$<?>[] cla$es) {  \n" //
        + "final String[] $ = new String[cla$es.length];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = cla$es[i] == null ? null : cla$es[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Cla$<?>[] cs) {  \n" //
                + "final String[] $ = new String[cs.length];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = cs[i] == null ? null : cs[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  @Test public void test02() {
    trimmingOf("public static String combine(final Uno<?>[] uno) {  \n" //
        + "final String[] $ = new String[uno.length];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = uno[i] == null ? null : uno[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Uno<?>[] us) {  \n" //
                + "final String[] $ = new String[us.length];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = us[i] == null ? null : us[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  @Test public void test03() {
    trimmingOf("public static String combine(final Many<?>[] manies) {  \n" //
        + "final String[] $ = new String[manies.length];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = manies[i] == null ? null : manies[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Many<?>[] ms) {  \n" //
                + "final String[] $ = new String[ms.length];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = ms[i] == null ? null : ms[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  @Test public void test04() {
    trimmingOf("public static String combine(final Many<? extends Few>[] fews) {  \n" //
        + "final String[] $ = new String[fews.length];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = fews[i] == null ? null : fews[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Many<? extends Few>[] fs) {  \n" //
                + "final String[] $ = new String[fs.length];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = fs[i] == null ? null : fs[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  // same test, with super instead of extends.
  @Test public void test05() {
    trimmingOf("public static String combine(final Many<? super Few>[] fews) {  \n" //
        + "final String[] $ = new String[fews.length];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = fews[i] == null ? null : fews[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Many<? super Few>[] fs) {  \n" //
                + "final String[] $ = new String[fs.length];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = fs[i] == null ? null : fs[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  // Parameterized that are not of some Collection __, don'tipper get an
  // 's' if
  // they're not an array.
  @Test public void test06() {
    trimmingOf("public static String combine(final Many<Paranoid> paranoid) {  \n" //
        + "final String[] $ = new String[paranoid.height()];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = paranoid == null ? null : paranoid.getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Many<Paranoid> p) {  \n" //
                + "final String[] $ = new String[p.height()];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = p == null ? null : p.getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  // Parameterized Collections do get an additional 's'.
  @Test public void test07() {
    trimmingOf("public static String combine(final List<Paranoid> paranoid) {  \n" //
        + "final String[] $ = new String[paranoid.length()];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = paranoid[i] == null ? null : paranoid[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final List<Paranoid> ps) {  \n" //
                + "final String[] $ = new String[ps.length()];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = ps[i] == null ? null : ps[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  @Test public void test08() {
    trimmingOf("public static String combine(final Set<Paranoid> paranoid) {  \n" //
        + "final String[] $ = new String[paranoid.size()];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = paranoid[i] == null ? null : paranoid[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Set<Paranoid> ps) {  \n" //
                + "final String[] $ = new String[ps.size()];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = ps[i] == null ? null : ps[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }

  // Collections of collections of arrays of Collections behave as expected.
  @Test public void test09() {
    trimmingOf("public static String combine(final Set<List<HashSet<?>[]>> hash) {  \n" //
        + "final String[] $ = new String[hash.size()];  \n" //
        + "for (int i = 0; i <$.length; ++i)  \n" //
        + "$[i] = hash[i] == null ? null : hash[i].getName(); \n" //
        + "return combine($);  \n" //
        + "}")
            .gives("public static String combine(final Set<List<HashSet<?>[]>> ossss) {  \n" //
                + "final String[] $ = new String[ossss.size()];  \n" //
                + "for (int i = 0; i <$.length; ++i)  \n" //
                + "$[i] = ossss[i] == null ? null : ossss[i].getName(); \n" //
                + "return combine($);  \n" //
                + "}");
  }
}
