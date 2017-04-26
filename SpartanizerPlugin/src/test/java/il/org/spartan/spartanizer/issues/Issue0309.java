package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Tomer Dragucki
 * @since 2016 */
public class Issue0309 {
  @Test @SuppressWarnings("static-method") public void a() {
    trimmingOf("public String abbreviate() { String ¢ = \"\"; "
        + "for (final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName); ; ¢ += m.group()) if (!m.find()) return ¢.toLowerCase(); }")
            .gives("public String abbreviate() { String $ = \"\"; "
                + "for (final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName); ; $ += m.group()) if (!m.find()) "
                + "return $.toLowerCase(); }")
            .gives("public String abbreviate() { String $ = \"\"; "
                + "for (final Matcher ¢ = Pattern.compile(\"[A-Z]\").matcher(typeName); ; $ += ¢.group()) if (!¢.find()) "
                + "return $.toLowerCase(); }");
  }

  @Test @SuppressWarnings("static-method") public void b() {
    trimmingOf("int checkIfCentChangesToDollarWhenNeeded() { Integer ¢ = 2; for (int i = 1; i < 7; ++i) ¢ *= i; return ¢.intValue(); }")
        .gives("int checkIfCentChangesToDollarWhenNeeded() { Integer $ = 2; for (int i = 1; i < 7; ++i) $ *= i; return $.intValue(); }")
        .gives("int checkIfCentChangesToDollarWhenNeeded() { Integer $ = 2; for (int ¢ = 1; ¢ < 7; ++¢) $ *= ¢; return $.intValue(); }");
  }
}
