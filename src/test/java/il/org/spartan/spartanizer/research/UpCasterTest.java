package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class UpCasterTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new UpCaster();
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
    assert not("@Override public boolean unfiltered(A a){}");
  }

  @Test public void b() {
    assert is("public static int hashCode(char value){return value;  }");
  }

  @Test public void c() {
    assert not("public static int hashCode(int value){return value;  }");
  }

  @Test public void d() {
    assert not("public static A hashCode(B value){return (A)value;  }");
  }
  
  @Test public void e() {
    assert not("public static A hashCode(B value){return value + value;  }");
  }
  
  @Test public void f() {
    assert not("public static A hashCode(B value){return value();  }");
  }

  private static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
