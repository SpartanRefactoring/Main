package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class DoNothingReturnThisTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new DoNothingReturnThis();
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
    assert is("boolean foo(boolean a){return this;}");
  }

  @Test public void b() {
    assert not("boolean foo(){return this();}");
  }

  @Test public void c() {
    assert not("@Override public int hashCode(Object b) {return this.b;}");
  }

  @Test public void d() {
    assert not("@Override public X unfiltered(int a, int b){  return a;}");
  }

  @Test public void e() {
    assert not("@Override public void unfiltered(Object a){  return;}");
  }

  private static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
