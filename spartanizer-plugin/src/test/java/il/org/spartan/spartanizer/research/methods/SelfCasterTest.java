package il.org.spartan.spartanizer.research.methods;

import org.junit.BeforeClass;
import org.junit.Test;

import il.org.spartan.spartanizer.research.nanos.methods.SelfCaster;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class SelfCasterTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new SelfCaster());
  }
  @Test public void a() {
    assert not("@Override public boolean unfiltered(A a){}");
  }
  @Test public void b() {
    assert not("public static int hashCode(char value){return value;  }");
  }
  @Test public void c() {
    assert is("@SuppressWarnings(\"unchecked\") <T>Predicate<T> withNarrowedType(){  return (Predicate<T>)this;}");
  }
}
