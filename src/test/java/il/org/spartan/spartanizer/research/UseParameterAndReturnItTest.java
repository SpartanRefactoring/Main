package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class UseParameterAndReturnItTest {
  private static final JavadocMarkerNanoPattern<MethodDeclaration> JAVADOCER = new UseParameterAndReturnIt();
  static final InteractiveSpartanizer spartanizer = new InteractiveSpartanizer();

  private static boolean javadoced(final String ¢) {
    return spartanized(¢).contains("[[" + JAVADOCER.getClass().getSimpleName() + "]]");
  }

  /** @param s
   * @return */
  private static boolean not(final String ¢) {
    return !returnsParam(¢);
  }

  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER);
  }

  private static String spartanized(final String ¢) {
    return spartanizer.fixedPoint(makeAST.COMPILATION_UNIT.from(¢) + "");
  }

  @Test public void a() {
    assert returnsParam("boolean foo(int a){return a;}");
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
    assert returnsParam("@Override public SetMultimap<K,V> unfiltered(A a){  if(a==null) print(a); else print(omg); return a;}");
  }

  @Test public void g() {
    assert returnsParam("@Override public SetMultimap<K,V> unfiltered(A a){  if(a==null) return a; use(); print(omg); return a;}");
  }

  private static boolean returnsParam(final String ¢) {
    return javadoced("public class A{" + ¢ + "}");
  }
}
