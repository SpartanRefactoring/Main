package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link Thrower}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class ThrowerTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Thrower());
  }

  @Test public void a() {
    assert is("boolean foo(){throw new OMG(I, \"have\", prob(lem));}");
  }

  @Test public void b() {
    assert is("void foo(){throw new Exception();}");
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

  @Test public void f() {
    assert is("void foo(Par am){throw new Exception();}");
  }

  @Test public void g() {
    assert is("@Override public void add(T element){    throw new UnsupportedOperationException();  }");
  }
}
