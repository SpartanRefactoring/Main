package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ConstantReturnerTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new ConstantReturner();
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  private static boolean yes(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }

  /** @param s
   * @return */
  private static boolean not(final String ¢) {
    return !yes(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER);
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void a() {
    assert yes("boolean foo(){return 3;}");
  }

  @Test public void b() {
    assert yes("boolean foo(){return \"omg\";}");
  }

  @Test public void c() {
    assert not("boolean foo(){return;}");
  }

  @Test public void d() {
    assert not("boolean foo(){print(x); return 2;}");
  }

  @Test public void e() {
    assert not("@Override public <T>HashCode hashObject(T instance,Funnel<? super T> t){return x;}");
  }

  @Test public void f() {
    assert yes("boolean foo(){return -3;}");
  }
}
