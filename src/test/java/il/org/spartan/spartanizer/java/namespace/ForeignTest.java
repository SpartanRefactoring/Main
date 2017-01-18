package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.spartanizer.meta.*;

@RunWith(Parameterized.class)
public class ForeignTest extends MetaFixture {
  final String repository;
  static final String FOREIGN = foreign.class.getSimpleName() + "";

  public ForeignTest(final SimpleName name, final String foreign, final String repository) {
    this.name = name;
    this.foreign = foreign;
    this.repository = repository;
  }

  @knows({ "type KnowsTest", "KnowsTest/2", "foreign", "foreign" }) private final String foreign;
  @knows({ "type KnowsTest", "recognizes/0", "name" }) private final SimpleName name;

  @Test public void foreign() {
    assert !Environment.of(name).has(foreign) : //
    "\n name = " + name + //
        "\n should not know = " + foreign + //
        "\n environment = " + Environment.of(name) + //
        "\n environment ancestry = " + Environment.of(name).ancestry() + //
        "\n enviroment children = " + Environment.of(name).description() + //
        "\n ancestry(name) = " + ancestry(name) //
    ;
  }

  @Parameters(name = "{index}. {0} does not know {1} ({2})") //
  public static Collection<Object[]> data() {
    return collect(FOREIGN, fixtures);
  }
}