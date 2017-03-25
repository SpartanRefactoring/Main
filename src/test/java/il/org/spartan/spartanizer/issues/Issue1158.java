package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/**
 * @author kobybs 
 * @since 2017-03-24
 */
@SuppressWarnings("static-method")
public class Issue1158 {
  @Test public void t1() {
    trimmingOf("@B // comment\n"
        + "@A int a() {\n"
        + "return b() && c();\n"
        + "}")
    .gives("@A\n"
        + "@B int a() {\n"
        + "return b() && c();\n"
        + "}");
  }
}
