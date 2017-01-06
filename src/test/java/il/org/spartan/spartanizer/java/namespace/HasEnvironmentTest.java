package il.org.spartan.spartanizer.java.namespace;

import java.util.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

@RunWith(Parameterized.class)
public class HasEnvironmentTest extends ReflectiveTester {
  public HasEnvironmentTest(final ASTNode name, @SuppressWarnings("unused") final String signature) {
    this.name = name;
  }

  private final ASTNode name;

  @Test public void notNullNode() {
    assert Environment.of(name) != null : //
    "\n name = " + name + //
        ReflectiveTester.ancestry(name) + //
        "\n\t environment = " + Environment.of(name)//
    ;
  }

  private static Set<String> signature = new HashSet<>();

  private static Collection<Object[]> collect(final ReflectiveTester... ts) {
    signature.clear();
    final List<Object[]> $ = new ArrayList<>();
    for (final ReflectiveTester t : ts)
      for (final ASTNode ¢ : searchDescendants.forClass(ASTNode.class).from(t.myCompilationUnit()))
        if (!signature.contains(signature(¢))) {
          signature.add(signature(¢));
          $.add(as.array(¢, signature(¢)));
        }
    return $;
  }

  @Parameters(name = "{index}. {1}: {0} ") public static Collection<Object[]> data() {
    return collect(new NamespaceTest(), new definitionTest());
  }

  private static String signature(final ASTNode ¢) {
    return separate.these(typeString(¢), typeString(parent(¢)), typeString(parent(parent(¢)))).by('/');
  }
}