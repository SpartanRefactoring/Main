package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class DefaultparametersAdderTest {
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadocedDefaulter(final String ¢) {
    return spartanized(¢).contains("[[DefaultParametersAdder]]");
  }

  /** @param s
   * @return */
  private static boolean notDefaulter(final String ¢) {
    return !defaulter(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, new DefaultParametersAdder());
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void a() {
    assert defaulter("boolean foo(){return foo(a);}");
  }

  @Test public void b() {
    assert notDefaulter("boolean foo(){return foo();}");
  }

  @Test public void c() {
    assert defaulter("@Override public int hashCode() {return Objects.hashCode(function, resultEquivalence);}");
  }

  private static boolean defaulter(final String ¢) {
    return javadocedDefaulter("public class A{" + ¢ + "}");
  }
}
