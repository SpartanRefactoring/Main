package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.Getter;

/** Tests {@link Getter}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class GetterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Getter());
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
