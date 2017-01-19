package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Yossi Gil
 * @since 2016-12 */
public interface scope {
  static ASTNode delimiter(final ASTNode ¢) {
    for (final ASTNode $ : ancestors.of(¢))
      switch ($.getNodeType()) {
        case ASTNode.BLOCK:
          return $;
        default:
          continue;
      }
    return null;
  }

  static List<ASTNode> of(final SingleVariableDeclaration x) {
    final ArrayList<ASTNode> $ = new ArrayList<>();
    $.add(x);
    return $;
  }

  static List<ASTNode> of(final VariableDeclarationFragment ¢) {
    final ArrayList<ASTNode> $ = new ArrayList<>();
    $.add(¢.getInitializer());
    return $;
  }

  static List<? extends ASTNode> of(final SimpleName ¢) {
    final List<? extends ASTNode> $ = definition.scope(¢);
    assert $ != null : fault.dump() + //
        "\n\t n=" + ¢ + //
        "\n\t definition.kind() = " + definition.kind(¢) + //
        fault.done();
    return $;
  }

  static Namespace getScopeNamespace(final ASTNode ¢) {
    return new Namespace(Environment.of(last(statements(az.block(delimiter(¢))))));
  }
  
  static String newName(final ASTNode ¢, final Type t) {
    return getScopeNamespace(¢).generateName(t);
  }
  
  static boolean hasInScope(final ASTNode ¢, String identifier) {
    return getScopeNamespace(¢).has(identifier);
  }
}
