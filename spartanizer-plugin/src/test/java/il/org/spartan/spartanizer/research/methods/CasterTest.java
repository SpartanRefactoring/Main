package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.Down.Caster;
import il.org.spartan.spartanizer.research.nanos.methods.Up;

/** Tests {@link Caster}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class CasterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new Up.Caster());
  }
  @Test public void a() {
    assert not("@Override public boolean unfiltered(A a){}");
  }
  @Test public void b() {
    assert is("public static int hashCode(char value){return value;  }");
  }
  @Test public void c() {
    assert not("public static int hashCode(int value){return value;  }");
  }
  @Test public void d() {
    assert is("private A cast(B value){return (A)value;  }");
  }
  @Test public void e() {
    assert not("public static A hashCode(B value){return value + value;  }");
  }
  @Test public void f() {
    assert not("public static A hashCode(B value){return value();  }");
  }
  @Test public void g() {
    assert not("public static int hashCode(char value){return this;  }");
  }
  @Test public void h() {
    assert not("public A cast(B value){return (A)this;  }");
  }
}
