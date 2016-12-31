package il.org.spartan.spartanizer.java.namespace;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

@RunWith(Parameterized.class)
public class KnowsTest extends ReflectiveTester {
  static final String KNOWN = knows.class.getSimpleName() + "";
  final String repository;

  public KnowsTest(final SimpleName name, final String shouldKnow, final String repository) {
    this.name = name;
    this.shouldKnow = shouldKnow;
    this.repository = repository;
  }

  @knows({ "type KnowsTest", "KnowsTest/3", "shouldKnow", "shouldKnow" }) private final String shouldKnow;
  @knows({ "type KnowsTest", "recognizes/0", "name" }) private final SimpleName name;

  @Test public void recognizes() {
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
    return collect(//
        // new NamespaceTest(), //
        // new definitionTest(), //
        new KnowsTest(null, null, null)//
    );
  }

  public int f(final int parameter) {
    @knows("parameter") final int $ = parameter * hashCode();
    return $ >>> $ * parameter;
  }

  public static Double g(double y) {
    final DoubleFunction<Double> $ = (x -> Double.valueOf(Math.sin(x * new Object() {
      @Override @knows({ "$", "g/1", "f/1", "x", "y" }) public int hashCode() {
        return g(y).hashCode();
      }
    }.hashCode())));
    return $.apply(y);
  }

  public static int g(int x, int y) {
    @knows({ "x", "y", "$" }) int $ = x * y;
    @knows({ "x", "y", "z", "$" }) int z = $ * (x + y);
    return x * z + y + $;
  }
  
  public static int h(int x, int y) {
    @knows({ "x", "y", "$" }) int $ = x * y;
    @knows({ "x", "y", "z", "$" }) int z = $ * (x + y);
    return x * z + y + $;
  }

  private static Collection<Object[]> collect(final ReflectiveTester... ts) {
    @knows({ "ts", "shouldKnow", "collect/1" }) final List<Object[]> $ = new ArrayList<>();
    for (final ReflectiveTester t : ts)
      for (final SingleMemberAnnotation a : t.singleMemberAnnotations())
        if ((a.getTypeName() + "").equals(KNOWN))
          for (final String s : values(a))
            for (final SimpleName ¢ : annotees.of(a))
              $.add(as.array(¢, s, t.getClass().getSimpleName()));
    return $;
  }
}