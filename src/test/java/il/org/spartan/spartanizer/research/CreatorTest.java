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
public class CreatorTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new Creator();
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  private static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
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
    assert is("boolean foo(){return new Object();}");
  }

  @Test public void b() {
    assert is("boolean foo(){return new Object(a);}");
  }

  @Test public void c() {
    assert not("boolean foo(){return new Object(a).c;}");
  }

  @Test public void d() {
    assert not("boolean foo(){return \"\" + new Object(a).c;}");
  }

  @Test public void e() {
    assert not("@Override public <T>HashCode hashObject(T instance,Funnel<? super T> t){ return newHasher().putObject(instance,t).hash(); }");
  }
}
