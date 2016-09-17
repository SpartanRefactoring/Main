package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.dispatch.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@Ignore @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue142Test {
  @Test public void disableSpartanizaionInClass() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInClass1() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInMethod() {
    trimming("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /***/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionInMethod1() {
    trimming("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /***/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler1() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass1() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod1() {
    trimming("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .to("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }
}
