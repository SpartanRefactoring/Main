package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.wring.dispatch.*;

/** Unit tests for {@link DisabledChecker}
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue142Test {
  @Test public void disableSpartanizaionInClass() {
    trimming("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}").stays();
  }

  @Test public void disableSpartanizaionInMethod() {
    trimming("/***/ class A {\n" + "  /**@DisableSpartan*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
        + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/***/ class A {\n" + "  /**@DisableSpartan*/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n" + "  /***/ int g() {\n"
                + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnabler() {
    trimming("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**@EnableSpartan*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "}")
            .to("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**@EnableSpartan*/ int g() {\n" + "    return 2;\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInClass() {
    trimming("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**@EnableSpartan*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /**@EnableSpartan*/ class B {\n"
        + "    /***/ int f() {\n" + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /***/ int g() {\n" + "      int $ = 2;\n"
        + "      return $;\n" + "    }\n" + "  }\n" + "}")
            .to("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**@EnableSpartan*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /**@EnableSpartan*/ class B {\n" + "    /***/ int f() {\n"
                + "      return 1;\n" + "    }\n" + "    /***/ int g() {\n" + "      return 2;\n" + "    }\n" + "  }\n" + "}");
  }

  @Test public void disableSpartanizaionWithEnablerDepthInMethod() {
    trimming("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
        + "  /**@EnableSpartan*/ int g() {\n" + "    int $ = 2;\n" + "    return $;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
        + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**@EnableSpartan*/ int g() {\n" + "      int $ = 2;\n" + "      return $;\n"
        + "    }\n" + "  }\n" + "}")
            .to("/**@DisableSpartan*/ class A {\n" + "  /***/ int f() {\n" + "    int $ = 1;\n" + "    return $;\n" + "  }\n"
                + "  /**@EnableSpartan*/ int g() {\n" + "    return 2;\n" + "  }\n" + "  /***/ class B {\n" + "    /***/ int f() {\n"
                + "      int $ = 1;\n" + "      return $;\n" + "    }\n" + "    /**@EnableSpartan*/ int g() {\n" + "      return 2;\n" + "    }\n"
                + "  }\n" + "}");
  }
}
