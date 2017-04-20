package il.org.spartan.spartanizer.java.namespace;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.spartanizer.meta.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-20 */
@RunWith(Parameterized.class)
public class KnowsTest extends MetaFixture {
  static final String KNOWN = knows.class.getSimpleName();

  @Parameters(name = "{index}. {0} knows {1} ({2})") public static Collection<Object[]> data() {
    return collect(KNOWN, fixtures);
  }

  public static Double g(final double y) {
    final DoubleFunction<Double> $ = ¢ -> Double.valueOf(Math.sin(¢ * new Object() {
      @Override @knows({ "$", "g/1", "f/1", "y" }) public int hashCode() {
        return g(y).hashCode();
      }
    }.hashCode()));
    return $.apply(y);
  }

  public static int g(final int x, final int y) {
    @knows({ "x", "y", "$" }) final int $ = x * y;
    @knows({ "x", "y", "z", "$" }) final int z = $ * (x + y);
    return x * z + y + $;
  }

  public static int h(final int x, final int y) {
    @knows({ "x", "y", "$" }) final int $ = x * y;
    @knows({ "x", "y", "z", "$" }) final int z = $ * (x + y);
    return x * z + y + $;
  }

  final String repository;
  @knows({ "__ KnowsTest", "KnowsTest/3", "shouldKnow", "shouldKnow" }) private final String shouldKnow;
  @knows({ "__ KnowsTest", "knows/0", "name" }) private final SimpleName name;

  public KnowsTest(final SimpleName name, final String shouldKnow, final String repository) {
    this.name = name;
    this.shouldKnow = shouldKnow;
    this.repository = repository;
  }

  public int f(final int parameter) {
    @knows("parameter") final int $ = parameter * hashCode();
    return $ >>> $ * parameter;
  }

  @Test public void knows() {
    assert Environment.of(name).has(shouldKnow) : //
    "\n name = " + name + //
        "\n should know = " + shouldKnow + //
        "\n environment = " + Environment.of(name) + //
        "\n environment ancestry = " + Environment.of(name).ancestry() + //
        "\n enviroment children = " + Environment.of(name).description() + //
        "\n ancestry(name) = " + ancestry(name) //
    ;
  }
}
