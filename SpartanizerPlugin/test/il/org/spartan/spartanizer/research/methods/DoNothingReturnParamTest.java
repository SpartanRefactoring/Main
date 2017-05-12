package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Tests {@link DoNothingReturnParam}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DoNothingReturnParamTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new DoNothingReturnParam());
  }

  @Test public void a() {
    assert is("boolean foo(boolean a){return a;}");
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
    assert is("A foo(A a){return a;}");
  }

  @Test public void g() {
    assert not("@Override public boolean unfiltered(A a){}");
  }

  @Test public void h() {
    assert not("public static int hashCode(char value){return value;  }");
  }
}
