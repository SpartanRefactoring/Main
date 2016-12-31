package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@Ignore
@RunWith(Parameterized.class)
public class AboveTest extends ReflectiveTester {
  static final String ABOVE = above.class.getSimpleName() + "";

  public AboveTest(final SimpleName name, final String above) {
    this.name = name;
    this.above = above;
  }

  private final String above;
  private final SimpleName name;

  @Test public void notNullParentParentNode() {
    assert Environment.of(name.getParent().getParent()) != null : "\n name = " + name + //
        "\n expected = " + above + //
        definitionTest.ancestry(name) + //
        "\n\t environment = " + Environment.of(name.getRoot())//
    ;
  }

  @Test public void notNullParentNode() {
    assert Environment.of(name.getParent()) != null : "\n name = " + name + //
        "\n expected = " + above + //
        definitionTest.ancestry(name) + //
        "\n\t environment = " + Environment.of(name.getRoot())//
    ;
  }

  @Test public void notNullRootNode() {
    assert Environment.of(name.getRoot()) != null : "\n name = " + name + //
        "\n expected = " + above + //
        definitionTest.ancestry(name) + //
        "\n\t environment = " + Environment.of(name.getRoot())//
    ;
  }

  @Test public void notNullNode() {
    assert Environment.of(name) != null : "\n name = " + name + //
        "\n expected = " + above + //
        definitionTest.ancestry(name) + //
        "\n\t environment = " + Environment.of(name)//
    ;
  }

  @Test public void isAbove() {
    assert Environment.of(name).has(above) : "\n name = " + name + //
        "\n expected = " + above + //
        definitionTest.ancestry(name) + //
        "\n\t environment = " + Environment.of(name)//
    ;
  }

  @Parameters(name = "{index} {1} above {0}") public static Collection<Object[]> data() {
    final List<Object[]> $ = new ArrayList<>();
    for (final SingleMemberAnnotation a : new NamespaceTest().singleMemberAnnotations())
      if ((a.getTypeName() + "").equals(ABOVE))
        for (final SimpleName ¢ : annotees.of(a))
          $.add(as.array(¢, az.stringLiteral(a.getValue()).getLiteralValue()));
    return $;
  }
}