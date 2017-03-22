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
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.meta.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-01 */
@RunWith(Parameterized.class)
public class ScopeSizeTest extends MetaFixture {
  static final String SCOPE_SIZE = ScopeSize.class.getSimpleName() + "";

  @Parameters(name = "{index} {0}/{2}={1}") public static Collection<Object[]> data() {
    final Collection<Object[]> $ = new ArrayList<>();
    for (final Annotation a : new definitionTest().annotations()) {
      final SingleMemberAnnotation sma = az.singleMemberAnnotation(a);
      if (sma != null && (sma.getTypeName() + "").equals(SCOPE_SIZE)) {
        int expected = MetaFixture.value(sma);
        for (final SimpleName ¢ : annotees.of(sma)) {
          $.add(as.array(¢, Integer.valueOf(expected), definition.kind(¢)));
          if (definition.kind(¢) != definition.Kind.field)
            --expected;
        }
      }
    }
    return $;
  }

  private final SimpleName name;
  private final Integer scopeSize;
  private final definition.Kind kind;

  public ScopeSizeTest(final SimpleName name, final Integer ScopeSize, final definition.Kind kind) {
    assert name != null;
    assert ScopeSize != null;
    this.name = name;
    scopeSize = ScopeSize;
    this.kind = kind;
  }

  @Test public void test() {
    azzert.that(
        "\n name = " + name + //
            "\n expected = " + scopeSize + //
            "\n got = " + scope.of(name).size() + //
            "\n\t kind = " + kind + //
            MetaFixture.ancestry(name) + //
            "\n\t scope = " + scope.of(name)//
        , scope.of(name).size(), is(scopeSize.intValue()));
  }
}
