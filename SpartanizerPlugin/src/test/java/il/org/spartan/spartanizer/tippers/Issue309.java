package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Tomer Dragucki
 * @since 2016 */
public class Issue309 {
  @SuppressWarnings("static-method") @Test public void a() {
    trimmingOf("public String abbreviate() { " + "String ¢ = \"\"; "
        + "for (final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName); ; ¢ += m.group()) " + "if (!m.find()) " + "return ¢.toLowerCase(); "
        + "}")
            .gives("public String abbreviate() { " + "String $ = \"\"; "
                + "for (final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName); ; $ += m.group()) " + "if (!m.find()) "
                + "return $.toLowerCase(); " + "}")
            .gives("public String abbreviate() { " + "String $ = \"\"; "
                + "for (final Matcher ¢ = Pattern.compile(\"[A-Z]\").matcher(typeName); ; $ += ¢.group()) " + "if (!¢.find()) "
                + "return $.toLowerCase(); " + "}");
  }

  @SuppressWarnings("static-method") @Test public void b() {
    trimmingOf("int checkIfCentChangesToDollarWhenNeeded() { " + "Integer ¢ = 2; " + "for (int i = 1; i < 7; ++i) " + "¢ *= i; "
        + "return ¢.intValue(); " + "}")
            .gives("int checkIfCentChangesToDollarWhenNeeded() { " + "Integer $ = 2; " + "for (int i = 1; i < 7; ++i) " + "$ *= i; "
                + "return $.intValue(); " + "}")
            .gives("int checkIfCentChangesToDollarWhenNeeded() { " + "Integer $ = 2; " + "for (int ¢ = 1; ¢ < 7; ++¢) " + "$ *= ¢; "
                + "return $.intValue(); " + "}");
  }
}
