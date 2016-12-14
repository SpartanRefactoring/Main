package il.org.spartan.spartanizer.research.methods;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import il.org.spartan.spartanizer.research.patterns.methods.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ToStringMethodTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new ToStringMethod();
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  /** @param s
   * @return */
  private static boolean not(final String ¢) {
    return !is(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER);
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void a() {
    assert is("String toString(){return foo;}");
  }

  @Test public void b() {
    assert not("boolean toString(){return foo();}");
  }

  @Test public void c() {
    assert not("@Override public String hashCode() {return b;}");
  }

  @Test public void d() {
    assert is("@Override public String toString(Par am){  return b}");
  }

  @Test public void e() {
    assert is("String toString(){return \"oh got\" + myVonderfull(bar);}");
  }

  private static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
