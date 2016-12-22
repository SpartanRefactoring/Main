package il.org.spartan.spartanizer.research.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since Dec 18, 2016 */
public class JavadocerTest {
  protected static JavadocMarkerNanoPattern JAVADOCER; // Descendants must
                                                       // initialize
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  protected static void setNano(final JavadocMarkerNanoPattern ¢) {
    spartanizer.add(MethodDeclaration.class, JAVADOCER = ¢);
  }

  static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }

  static boolean not(final String ¢) {
    return !is(¢);
  }
}
