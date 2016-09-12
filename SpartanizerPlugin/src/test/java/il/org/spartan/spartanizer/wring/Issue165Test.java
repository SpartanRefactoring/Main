package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue165Test {
  @Test public void seriesA_01() {
    trimming(//
        " public static boolean __final(final VariableDeclarationStatement s) {\n" //
            + "return (Modifier.FINAL & s.getModifiers()) != 0;}").to(//
                " public static boolean __final(final VariableDeclarationStatement ¢) {\n" //
                    + "return (Modifier.FINAL & ¢.getModifiers()) != 0;}");
  }

}
