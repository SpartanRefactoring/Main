package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for Renaming Bug, Issue 157.
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method") public final class Issue157Test {
  @Test public void test01() {
    trimming(" public static String combine(final Class<?>[] classes) {  \n" //
        + "final String[] ss = new String[classes.length];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = classes[i] == null ? null : classes[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Class<?>[] cs) {  \n" //
                + "final String[] ss = new String[cs.length];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = cs[i] == null ? null : cs[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  @Test public void test02() {
    trimming(" public static String combine(final Uno<?>[] uno) {  \n" //
        + "final String[] ss = new String[uno.length];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = uno[i] == null ? null : uno[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Uno<?>[] us) {  \n" //
                + "final String[] ss = new String[us.length];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = us[i] == null ? null : us[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  @Test public void test03() {
    trimming(" public static String combine(final Many<?>[] manies) {  \n" //
        + "final String[] ss = new String[manies.length];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = manies[i] == null ? null : manies[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Many<?>[] ms) {  \n" //
                + "final String[] ss = new String[ms.length];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = ms[i] == null ? null : ms[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  @Test public void test04() {
    trimming(" public static String combine(final Many<? extends Few>[] fews) {  \n" //
        + "final String[] ss = new String[fews.length];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = fews[i] == null ? null : fews[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Many<? extends Few>[] fs) {  \n" //
                + "final String[] ss = new String[fs.length];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = fs[i] == null ? null : fs[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  // same test, with super instead of extends.
  @Test public void test05() {
    trimming(" public static String combine(final Many<? super Few>[] fews) {  \n" //
        + "final String[] ss = new String[fews.length];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = fews[i] == null ? null : fews[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Many<? super Few>[] fs) {  \n" //
                + "final String[] ss = new String[fs.length];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = fs[i] == null ? null : fs[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  // Parameterized that are not of some Collection type, don't get an 's' if
  // they're not an array.
  @Test public void test06() {
    trimming(" public static String combine(final Many<Paranoid> paranoid) {  \n" //
        + "final String[] ss = new String[paranoid.height()];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = paranoid == null ? null : paranoid.getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Many<Paranoid> p) {  \n" //
                + "final String[] ss = new String[p.height()];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = p == null ? null : p.getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  // Parameterized Collections do get an additional 's'.
  @Test public void test07() {
    trimming(" public static String combine(final List<Paranoid> paranoid) {  \n" //
        + "final String[] ss = new String[paranoid.length()];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = paranoid[i] == null ? null : paranoid[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final List<Paranoid> ps) {  \n" //
                + "final String[] ss = new String[ps.length()];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = ps[i] == null ? null : ps[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  @Test public void test08() {
    trimming(" public static String combine(final Set<Paranoid> paranoid) {  \n" //
        + "final String[] ss = new String[paranoid.size()];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = paranoid[i] == null ? null : paranoid[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Set<Paranoid> ps) {  \n" //
                + "final String[] ss = new String[ps.size()];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = ps[i] == null ? null : ps[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }

  // Collections of collections of arrays of Collections behave as expected.
  @Test public void test09() {
    trimming(" public static String combine(final Set<List<HashSet<?>[]>> hash) {  \n" //
        + "final String[] ss = new String[hash.size()];  \n" //
        + "for (int i = 0; i < ss.length; ++i)  \n" //
        + "ss[i] = hash[i] == null ? null : hash[i].getName(); \n" //
        + "return combine(ss);  \n" //
        + "}")
            .to(" public static String combine(final Set<List<HashSet<?>[]>> sssss) {  \n" //
                + "final String[] ss = new String[sssss.size()];  \n" //
                + "for (int i = 0; i < ss.length; ++i)  \n" //
                + "ss[i] = sssss[i] == null ? null : sssss[i].getName(); \n" //
                + "return combine(ss);  \n" //
                + "}");
  }
}
