package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0142 {
  @Test public void disableSpartanizaionInClass() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }").stays();
  }

  @Test public void disableSpartanizaionInClass1() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }").stays();
  }

  @Test public void disableSpartanizaionInMethod() {
    trimminKof("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }")
        .gives("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { return 2; } }");
  }

  @Test public void disableSpartanizaionInMethod1() {
    trimminKof("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } }")
        .gives("/***/ class A { /**[[SuppressWarningsSpartan]]*/ int f() { int $ = 1; return $; }  /***/ int g() { return 2; } }");
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } }");
  }

  @Test public void disableSpartanizaionWithEnabler1() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } }");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ class B { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /**[[EnableWarningsSpartan]]*/ class B { "
                + " /***/ int f() { return 1; } /***/ int g() { return 2; } } }");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass1() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ class B { /***/ int f() { int $ = 1; return $; }  /***/ int g() { int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /**[[EnableWarningsSpartan]]*/ class B { "
                + " /***/ int f() { return 1; } /***/ int g() { return 2; } } }");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } /***/ class B { "
        + " /***/ int f() { int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() {  int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /***/ class B { /***/ int f() { "
                + " int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() { return 2;  } } }");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod1() {
    trimminKof("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
        + " /**[[EnableWarningsSpartan]]*/ int g() { int $ = 2; return $; } /***/ class B { "
        + " /***/ int f() { int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() {  int $ = 2; return $; } } }")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A { /***/ int f() { int $ = 1; return $; } "
                + " /**[[EnableWarningsSpartan]]*/ int g() { return 2; } /***/ class B { /***/ int f() { "
                + " int $ = 1; return $; } /**[[EnableWarningsSpartan]]*/ int g() { return 2;  } } }");
  }
}
