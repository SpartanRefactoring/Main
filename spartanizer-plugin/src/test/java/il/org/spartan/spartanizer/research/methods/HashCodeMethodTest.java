package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.HashCodeMethod;

/** Tests {@link HashCodeMethod}
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class HashCodeMethodTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new HashCodeMethod());
  }
  @Test public void a() {
    assert is("int hashCode(){return foo;}");
  }
  @Test public void b() {
    assert is("int hashCode(){return 3 + 7 + g(another);}");
  }
  @Test public void c() {
    assert not("@Override public String hashCode() {return b;}");
  }
  @Test public void d() {
    assert not("@Override public String toString(Par am){  return b}");
  }
  @Test public void e() {
    assert not("String toString(){return \"oh got\" + myVonderfull(bar);}");
  }
}
