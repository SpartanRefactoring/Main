package il.org.spartan.spartanizer.research.methods;

import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.methods.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

@SuppressWarnings("static-method")
public class DefaultparametersAdderTest extends JavadocerTest {
  @BeforeClass public static void setUp() {
    setNano(new DefaultParametersAdder());
  }

  @Test public void a() {
    assert is("boolean foo(){return foo(a);}");
  }

  @Test public void b() {
    assert not("boolean foo(){return foo();}");
  }

  @Test public void c() {
    assert is("@Override public int hashCode() {return Objects.hashCode(function, resultEquivalence);}");
  }

  @Test public void d() {
    assert is("boolean foo(){synchronized(mutex){return foo(a);}}");
  }

  @Test public void e() {
    assert is(
        "private static void logPatternCompilerError(ServiceConfigurationError ¢) {    logger.logPatternCompilerError(Level.WARNING, \"thingy\", ¢);}");
  }
}

