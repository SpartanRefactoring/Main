package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class DoNothingReturnParamTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new DoNothingReturnParam();
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  /** @param s
   * @return */
  private static boolean not(final String ¢) {
    return !doNothing(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER);
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void a() {
    assert doNothing("boolean foo(boolean a){return a;}");
  }

  @Test public void b() {
    assert not("boolean foo(){return foo();}");
  }

  @Test public void c() {
    assert not("@Override public int hashCode(Object b) {return this.b;}");
  }

  @Test public void d() {
    assert not("@Override public X unfiltered(int a, int b){  return a;}");
  }

  @Test public void e() {
    assert not("@Override public X unfiltered(Object a){  return (SetMultimap)a;}");
  }

  @Test public void f() {
    assert doNothing("A foo(A a){return a;}");
  }

  @Test public void g() {
    assert not("@Override public boolean unfiltered(A a){}");
  }

  @Test public void h() {
    assert not("public static int hashCode(char value){return value;  }");
  }

  private static boolean doNothing(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
