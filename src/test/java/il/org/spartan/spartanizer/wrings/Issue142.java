package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@Ignore @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue142 {
  @Test public void disableSpartanizaionInClass() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInClass1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInMethod() {
    trimmingOf("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /***/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionInMethod1() {
    trimmingOf("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /***/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /**[[SuppressWarningsSpartan]]*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /***/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod1() {
    trimmingOf("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/**[[SuppressWarningsSpartan]]*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }
}
