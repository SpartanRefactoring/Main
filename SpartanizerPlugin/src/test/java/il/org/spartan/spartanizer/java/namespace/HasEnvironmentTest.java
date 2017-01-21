package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.meta.*;

@RunWith(Parameterized.class)
public class HasEnvironmentTest extends MetaFixture {
  public HasEnvironmentTest(final ASTNode name, @SuppressWarnings("unused") final String signature) {
    this.name = name;
  }

  private final ASTNode name;

  @Test public void notNullNode() {
    assert Environment.of(name) != null : //
    "\n name = " + name + //
        MetaFixture.ancestry(name) + //
        "\n\t environment = " + Environment.of(name)//
    ;
  }

  private static final Set<String> signature = new HashSet<>();

  private static Collection<Object[]> collect(final MetaFixture... fs) {
    signature.clear();
    final List<Object[]> $ = new ArrayList<>();
    for (final MetaFixture t : fs)
      yieldDescendants.untilClass(ASTNode.class).from(t.reflectedCompilationUnit()).stream().filter(¢ -> !signature.contains(signature(¢)))
          .forEach(¢ -> {
            signature.add(signature(¢));
            $.add(as.array(¢, signature(¢)));
          });
    return $;
  }

  @Parameters(name = "{index}. {1}: {0} ") public static Collection<Object[]> data() {
    return collect(new NamespaceTest(), new definitionTest());
  }

  private static String signature(final ASTNode ¢) {
    return separate.these(typeString(¢), typeString(parent(¢)), typeString(parent(parent(¢)))).by('/');
  }
}