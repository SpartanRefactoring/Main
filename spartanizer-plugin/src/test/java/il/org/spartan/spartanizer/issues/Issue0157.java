package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for Renaming fault, Issue0 157.
 * @author  Dan Greenstein
 * @since  2016 
 */
@Ignore
@SuppressWarnings("static-method")
public final class Issue0157 {
  @Ignore @Test public void test01() {
    trimmingOf("public static String combine(final Cla$<?>[] cla$es) {  \nfinal String[] $ = new String[cla$es.length];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = cla$es[i] == null ? null : cla$es[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Cla$<?>[] cs) {  \nfinal String[] $ = new String[cs.length];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = cs[i] == null ? null : cs[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test02() {
    trimmingOf("public static String combine(final Uno<?>[] uno) {  \nfinal String[] $ = new String[uno.length];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = uno[i] == null ? null : uno[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Uno<?>[] us) {  \nfinal String[] $ = new String[us.length];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = us[i] == null ? null : us[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test03() {
    trimmingOf("public static String combine(final Many<?>[] manies) {  \nfinal String[] $ = new String[manies.length];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = manies[i] == null ? null : manies[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Many<?>[] ms) {  \nfinal String[] $ = new String[ms.length];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = ms[i] == null ? null : ms[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test04() {
    trimmingOf("public static String combine(final Many<? extends Few>[] fews) {  \nfinal String[] $ = new String[fews.length];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = fews[i] == null ? null : fews[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Many<? extends Few>[] fs) {  \nfinal String[] $ = new String[fs.length];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = fs[i] == null ? null : fs[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test05() {
    trimmingOf("public static String combine(final Many<? super Few>[] fews) {  \nfinal String[] $ = new String[fews.length];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = fews[i] == null ? null : fews[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Many<? super Few>[] fs) {  \nfinal String[] $ = new String[fs.length];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = fs[i] == null ? null : fs[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test06() {
    trimmingOf("public static String combine(final Many<Paranoid> paranoid) {  \nfinal String[] $ = new String[paranoid.height()];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = paranoid == null ? null : paranoid.getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Many<Paranoid> p) {  \nfinal String[] $ = new String[p.height()];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = p == null ? null : p.getName(); \nreturn combine($);  \n}");
  }
  @Test public void test07() {
    trimmingOf("public static String combine(final List<Paranoid> paranoid) {  \nfinal String[] $ = new String[paranoid.length()];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = paranoid[i] == null ? null : paranoid[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final List<Paranoid> ps) {  \nfinal String[] $ = new String[ps.length()];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = ps[i] == null ? null : ps[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test08() {
    trimmingOf("public static String combine(final Set<Paranoid> paranoid) {  \nfinal String[] $ = new String[paranoid.size()];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = paranoid[i] == null ? null : paranoid[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Set<Paranoid> ps) {  \nfinal String[] $ = new String[ps.size()];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = ps[i] == null ? null : ps[i].getName(); \nreturn combine($);  \n}");
  }
  @Test public void test09() {
    trimmingOf("public static String combine(final Set<List<HashSet<?>[]>> hash) {  \nfinal String[] $ = new String[hash.size()];  \n"
        + "for (int i = 0; i <$.length; ++i)  \n$[i] = hash[i] == null ? null : hash[i].getName(); \nreturn combine($);  \n}")
            .gives("public static String combine(final Set<List<HashSet<?>[]>> ossss) {  \nfinal String[] $ = new String[ossss.size()];  \n"
                + "for (int i = 0; i <$.length; ++i)  \n$[i] = ossss[i] == null ? null : ossss[i].getName(); \nreturn combine($);  \n}");
  }
}
