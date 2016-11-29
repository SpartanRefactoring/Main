package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
@Ignore
public final class Issue142 {
  @Test public void disableSpartanizaionInClass() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInClass1() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInMethod() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
                + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionInMethod1() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
                + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler1() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass1() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ class B {\n" + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n"
        + "    /***/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**[[EnableWarningsSpartan]]*/ class B {\n"
                + "    /***/ int f() {\n" + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n"
                + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod1() {
    trimmingOf("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n"
        + "      int $ = 2;\n" + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .gives("/***/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**[[EnableWarningsSpartan]]*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**[[EnableWarningsSpartan]]*/ int g() {\n" + "      return 2;\n"
                + "    }\n" + "  }\n" + "}");
  }
}
