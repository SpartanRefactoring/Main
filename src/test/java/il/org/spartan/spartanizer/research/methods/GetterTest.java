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
public class GetterTest {
  private static final JavadocMarkerNanoPattern JAVADOCER = new Getter();
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
    assert is("boolean foo(){return foo;}");
  }

  @Test public void b() {
    assert not("boolean foo(){return foo();}");
  }

  @Test public void c() {
    assert is("@Override public int hashCode() {return this.b;}");
  }

  @Test public void d() {
    assert is("@Override public X unfiltered(){  return (SetMultimap)unfiltered;}");
  }

  @Test public void e() {
    assert is("@Override public SetMultimap<K,V> unfiltered(){  return (SetMultimap<K,V>)unfiltered;}");
  }

  private static boolean is(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
