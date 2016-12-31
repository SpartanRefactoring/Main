package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

@RunWith(Parameterized.class)
public class ForeignTest extends ReflectiveTester {
  static final String FOREIGN = foreign.class.getSimpleName() + "";

  public ForeignTest(final SimpleName name, final String foreign) {
    this.name = name;
    this.foreign = foreign;
  }

  @knows({"type KnowsTest", "KnowsTest/2","foreign", "foreign"}) private final String foreign;
  @knows({"type KnowsTest", "recognizes/0", "name"}) private final SimpleName name;

  @Test public void foreign() {
    assert !Environment.of(name).has(foreign) : //
    "\n name = " + name + //
        "\n foreign = " + foreign + //
        "\n environment = " + Environment.of(name) + //
        "\n environment ancestry = " + Environment.of(name).ancestry() + //
        "\n enviroment children = " + Environment.of(name).description() + //
        "\n ancestry(name) = " + ancestry(name) //
    ;
  }

  @Parameters(name = "{index}. {0} recognizes {1} ") public static Collection<Object[]> data() {
    return collect(new NamespaceTest(), new definitionTest(), new ForeignTest(null, null));
  }

  private static Collection<Object[]> collect(final ReflectiveTester... ts) {
    @knows({"ts", "foreign", "collect/1"}) final List<Object[]> $ = new ArrayList<>();
    for (final ReflectiveTester t : ts)
      for (final SingleMemberAnnotation a : t.singleMemberAnnotations())
        if ((a.getTypeName() + "").equals(FOREIGN))
          for (final String s : values(a))
            for (final SimpleName ¢ : annotees.of(a))
              $.add(as.array(¢, s));
    return $;
  }
}