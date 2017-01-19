package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.meta.*;

@RunWith(Parameterized.class)
public class SingleMarkerTest extends MetaFixture {
  public SingleMarkerTest(final definition.Kind kind, final SimpleName name) {
    assert name != null;
    this.name = name;
    this.kind = kind;
  }

  private final SimpleName name;
  private final definition.Kind kind;

  @Test public void test() {
    azzert.that(
        "\n name = " + name + //
            "\n\t kind = " + kind + //
            MetaFixture.ancestry(name) + //
            "\n\t scope = " + scope.of(name)//
        , definition.kind(name), is(kind));
  }

  @Parameters(name = "{index}] {0} {1}") public static Collection<Object[]> data() {
    final List<Object[]> $ = new ArrayList<>();
    for (final MarkerAnnotation a : new definitionTest().markers()) {
      final String key = (a + "").substring(1);
      if (definition.Kind.has(key))
        annotees.of(a).forEach(¢ -> $.add(as.array(definition.Kind.valueOf(key), ¢)));
    }
    return $;
  }
}