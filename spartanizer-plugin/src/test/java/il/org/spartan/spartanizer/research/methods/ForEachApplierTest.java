package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.ForEachApplier;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ForEachApplierTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new ForEachApplier());
  }
  @Test public void a() {
    assert is("void invalidateAll(Iterable<?> keys){  keys.stream().forEach(key -> remove(key));}");
  }
  @Test public void b() {
    assert is("void invalidateAll(Iterable<?> keys){  keys.stream().filter(x -> x!= null).forEach(key -> remove(key));}");
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
