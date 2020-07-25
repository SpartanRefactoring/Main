/* TODO please add a description
 *
 * @author
 *
 * @since */
package il.org.spartan.spartanizer.java.namespace;

import static fluent.ly.azzert.is;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fluent.ly.as;
import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.navigate.annotees;
import il.org.spartan.spartanizer.meta.MetaFixture;

@RunWith(Parameterized.class)
public class SingleMarkerTest extends MetaFixture {
  @Parameters(name = "{index}] {0} {1}") public static Collection<Object[]> data() {
    final List<Object[]> $ = an.empty.list();
    for (final MarkerAnnotation a : new definitionTest().markers()) {
      final String key = (a + "").substring(1);
      if (definition.Kind.has(key))
        annotees.of(a).forEach(λ -> $.add(as.array(definition.Kind.valueOf(key), λ)));
    }
    return $;
  }

  private final SimpleName name;
  private final definition.Kind kind;

  public SingleMarkerTest(final definition.Kind kind, final SimpleName name) {
    assert name != null;
    this.name = name;
    this.kind = kind;
  }
  @Test public void test() {
    azzert.that(
        "\n name = " + name + //
            "\n\t kind = " + kind + //
            MetaFixture.ancestry(name) + //
            "\n\t scope = " + scope.of(name)//
        , definition.kind(name), is(kind));
  }
}
