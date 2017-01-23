package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

@SuppressWarnings("static-method")
public class ConstantReturnerTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new ConstantReturner());
  }

  @Test public void a() {
    assert is("boolean foo(){return 3;}");
  }

  @Test public void b() {
    assert is("boolean foo(){return \"omg\";}");
  }

  @Test public void c() {
    assert not("boolean foo(){return;}");
  }

  @Test public void d() {
    assert not("boolean foo(){print(x); return 2;}");
  }

  @Test public void e() {
    assert not("@Override public <T>HashCode hashObject(T instance,Funnel<? super T> t){return x;}");
  }

  @Test public void f() {
    assert is("boolean foo(){return -3;}");
  }

  @Test public void g() {
    assert not("int foo(){return 0;}");
  }
}

