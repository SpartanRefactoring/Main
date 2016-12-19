package il.org.spartan.spartanizer.research.methods;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.methods.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class GetterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    spartanizer.add(MethodDeclaration.class, JAVADOCER = new Getter());
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
}
