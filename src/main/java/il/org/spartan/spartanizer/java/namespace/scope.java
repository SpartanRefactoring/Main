package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-12 */
public interface scope {
  static ASTNode delimiter(final ASTNode ¢) {
    if (iz.block(¢) || iz.switchStatement(¢))
      return ¢;
    for (final ASTNode $ : ancestors.of(¢))
      switch (nodeType($)) {
        case ASTNode.BLOCK:
        case ASTNode.SWITCH_STATEMENT:
          return $;
        default:
      }
    return null;
  }

  static List<ASTNode> of(final SingleVariableDeclaration x) {
    final ArrayList<ASTNode> $ = new ArrayList<>();
    $.add(x);
    return $;
  }

  static List<? extends ASTNode> of(final VariableDeclarationFragment ¢) {
    return scope.of(¢.getName());
  }

  static List<? extends ASTNode> of(final SimpleName ¢) {
    final List<? extends ASTNode> $ = definition.scope(¢);
    assert $ != null : fault.dump() + //
        "\n\t n=" + ¢ + //
        "\n\t definition.kind() = " + definition.kind(¢) + //
        fault.done();
    return $;
  }

  static Block getBlock(final ASTNode ¢) {
    return az.block(delimiter(¢));
  }

  /** Bug in ternary spartanizing, do not remove the suppress
   * [[SuppressWarningsSpartan]] */
  static Namespace getScopeNamespace(final ASTNode ¢) {
    final ASTNode $ = delimiter(¢);
    return new Namespace(Environment.of(last(iz.block($) ? statements(az.block($)) : statements(az.switchStatement($)))));
  }

  static String newName(final ASTNode ¢, final Type t) {
    final ASTNode b = delimiter(¢);
    final Namespace n = b.getProperty("Namespace") == null ? getScopeNamespace(¢) : (Namespace) b.getProperty("Namespace");
    final String $ = n.generateName(t);
    n.addNewName($, t);
    b.setProperty("Namespace", n);
    return $;
  }

  static String newName(final ASTNode ¢, final Type t, final String s) {
    final ASTNode b = delimiter(¢);
    final Namespace n = b.getProperty("Namespace") == null ? getScopeNamespace(¢) : (Namespace) b.getProperty("Namespace");
    final String $ = n.generateName(s);
    n.addNewName($, t);
    b.setProperty("Namespace", n);
    return $;
  }

  static boolean hasInScope(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢).has(identifier);
  }
}
