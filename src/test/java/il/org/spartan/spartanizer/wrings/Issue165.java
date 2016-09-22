package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for centification of a single parameter to a function
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue165 {
  @Test public void seriesA_01_vanilla() {
    trimmingOf(//
        " public static boolean f(final VariableDeclarationStatement s) {\n" //
            + "return (Modifier.FINAL & s.getModifiers()) != 0;}").gives(//
                " public static boolean f(final VariableDeclarationStatement ¢) {\n" //
                    + "return (Modifier.FINAL & ¢.getModifiers()) != 0;}");
  }

  @Test public void seriesA_02_dollar() {
    trimmingOf(//
        " public static boolean __final(final VariableDeclarationStatement $) {\n" //
            + "return (Modifier.FINAL & $.getModifiers()) != 0;}").stays();
  }

  @Test public void seriesA_03_single_underscore() {
    trimmingOf("void f(int _){}").gives("void f(int __){}").stays();
  }

  @Test public void seriesA_04_double_underscore() {
    trimmingOf("void f(int __){}").stays();
  }

  @Test public void seriesA_05_unused() {
    trimmingOf("void f(int a){}").stays();
  }

  @Test public void seriesA_06_abstract() {
    trimmingOf("abstract void f(int a);").stays();
  }

  @Test public void seriesA_06_meaningfulName() {
    trimmingOf("void f(String fileName) {f(fileName);}").stays();
  }
}
