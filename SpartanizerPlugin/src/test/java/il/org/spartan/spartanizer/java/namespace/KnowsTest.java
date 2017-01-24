/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Jan 1, 2017 */
package il.org.spartan.spartanizer.java.namespace;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.spartanizer.meta.*;

@RunWith(Parameterized.class)
public class KnowsTest extends MetaFixture {
  static final String KNOWN = knows.class.getSimpleName() + "";
  final String repository;

  public KnowsTest(final SimpleName name, final String shouldKnow, final String repository) {
    this.name = name;
    this.shouldKnow = shouldKnow;
    this.repository = repository;
  }

  @knows({ "type KnowsTest", "KnowsTest/3", "shouldKnow", "shouldKnow" }) private final String shouldKnow;
  @knows({ "type KnowsTest", "knows/0", "name" }) private final SimpleName name;

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

  @Parameters(name = "{index}. {0} knows {1} ({2})") public static Collection<Object[]> data() {
    return collect(KNOWN, fixtures);
  }

  public int f(final int parameter) {
    @knows("parameter") final int $ = parameter * hashCode();
    return $ >>> $ * parameter;
  }

  public static Double g(final double y) {
    final DoubleFunction<Double> $ = x -> Double.valueOf(Math.sin(x * new Object() {
      @Override @knows({ "$", "g/1", "f/1", "x", "y" }) public int hashCode() {
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
}
