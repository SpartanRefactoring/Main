package il.org.spartan.spartanizer.research.methods;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 18, 2016 */
public abstract class JavadocerTest {
  protected static JavadocMarkerNanoPattern JAVADOCER; // Descendants must
                                                       // initialize
  static final SpartAnalyzer spartanizer = new SpartAnalyzer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  protected static void setNano(final JavadocMarkerNanoPattern ¢) {
    spartanizer.add(MethodDeclaration.class, JAVADOCER = ¢);
  }

  protected static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }

  protected static boolean not(final String ¢) {
    return !is(¢);
  }
}
