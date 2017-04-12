package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link DoNothingReturnThis}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DoNothingReturnThisTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new DoNothingReturnThis());
  }

  @Test public void a() {
    assert is("boolean foo(boolean a){return this;}");
  }

  @Test public void b() {
    assert not("boolean foo(){return this();}");
  }

  @Test public void c() {
    assert not("@Override public int hashCode(Object b) {return this.b;}");
  }

  @Test public void d() {
    assert not("@Override public X unfiltered(int a, int b){  return a;}");
  }

  @Test public void e() {
    assert not("@Override public void unfiltered(Object a){  return;}");
  }
}
