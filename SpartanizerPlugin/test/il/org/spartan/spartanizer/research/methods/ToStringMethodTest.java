package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ToStringMethodTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new ToStringMethod());
  }
  @Test public void a() {
    assert is("String toString(){return foo;}");
  }
  @Test public void b() {
    assert not("boolean toString(){return foo();}");
  }
  @Test public void c() {
    assert not("@Override public String hashCode() {return b;}");
  }
  @Test public void d() {
    assert is("@Override public String toString(Par am){  return b}");
  }
  @Test public void e() {
    assert is("String toString(){return \"oh got\" + myVonderfull(bar);}");
  }
}
