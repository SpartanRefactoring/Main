package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Checking that the tipper of inlining of infix and postfix expressions is
 * working well
 * @author Dor Ma'ayan
 * @since 25-11-2016 */
@SuppressWarnings("static-method")
public class Issue0363 {
  @Test public void test0() {
    trimmingOf("optarg = argv[optind]; ++optind;")//
        .gives("optarg = argv[optind++];");
  }
  @Test public void test1() {
    trimmingOf("j = argv[optind]+3; ++optind;")//
        .gives("j = argv[optind++]+3;");
  }
  @Test public void test2() {
    trimmingOf("j = b + argv[optind]+3; ++optind;")//
        .gives("j = b+argv[optind++]+3;");
  }
  @Test public void test3() {
    trimmingOf("j = b + argv[optindi]+3; ++optind;")//
        .stays();
  }
  @Test public void test4() {
    trimmingOf("j = b + argv[optind]+3; optind++;")//
        .gives("j = b + argv[optind]+3; ++optind;")//
        .gives("j = b+argv[optind++]+3;");
  }
}
