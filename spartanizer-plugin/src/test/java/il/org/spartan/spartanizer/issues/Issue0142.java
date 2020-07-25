package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0142 {
  @Test public void disableSpartanizaionInClass() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }").stays();
  }
  @Test public void disableSpartanizaionInClass1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }").stays();
  }
  @Test public void disableSpartanizaionInMethod() {
    trimmingOf("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }")
        .gives("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { return 2; } }");
  }
  @Test public void disableSpartanizaionInMethod1() {
    trimmingOf("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }")
        .gives("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { return 2; } }");
  }
  @Test public void disableSpartanizaionWithEnabler() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } }");
  }
  @Test public void disableSpartanizaionWithEnabler1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } }");
  }
  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ class B { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /**[[EnableWarningsSpartan]]*/ class B { "
                + " /***/ int f() { return 1; } /***/ int g() { return 2; } } }");
  }
  @Test public void disableSpartanizaionWithEnablerDepthInClass1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ class B { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /**[[EnableWarningsSpartan]]*/ class B { "
                + " /***/ int f() { return 1; } /***/ int g() { return 2; } } }");
  }
  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } /***/ class B { "
        + " /***/ int f() { int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() {  int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /***/ class B { /***/ int f() { "
                + " int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() { return 2;  } } }");
  }
  @Test public void disableSpartanizaionWithEnablerDepthInMethod1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } /***/ class B { "
        + " /***/ int f() { int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() {  int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /***/ class B { /***/ int f() { "
                + " int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() { return 2;  } } }");
  }
}
