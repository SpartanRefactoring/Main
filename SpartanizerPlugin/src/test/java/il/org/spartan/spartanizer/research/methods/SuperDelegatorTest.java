package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link SuperDelegator}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class SuperDelegatorTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new SuperDelegator());
  }
  @Test public void a() {
    assert is("boolean foo(){return super.foo();}");
  }
  @Test public void b() {
    assert not("boolean foo(){return foo();}");
  }
  @Test public void c() {
    assert not("@Override public int hashCode() {return this.b;}");
  }
  @Test public void d() {
    assert is("@Override final boolean foo(){return (A)super.foo();}");
  }
  @Test public void e() {
    assert is(
        " @Override void invokeSubscriberMethod(Object event) throws InvocationTargetException {synchronized (this) { super.invokeSubscriberMethod(event); }}");
  }
}
