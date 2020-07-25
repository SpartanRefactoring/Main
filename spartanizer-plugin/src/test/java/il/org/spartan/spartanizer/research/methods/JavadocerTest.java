package il.org.spartan.spartanizer.research.methods;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.research.analyses.Nanonizer;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** Base class for all tests for method patterns
 * @author Ori Marcovitch
 * @since Dec 18, 2016 */
public abstract class JavadocerTest {
  protected static JavadocMarkerNanoPattern JAVADOCER; // Descendants must
                                                       // initialize
  static final Nanonizer spartanizer = new Nanonizer();

  protected static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }
  protected static boolean not(final String ¢) {
    return !is(¢);
  }
  protected static void setNano(final JavadocMarkerNanoPattern ¢) {
    spartanizer.add(MethodDeclaration.class, JAVADOCER = ¢);
  }
  protected static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }
}
