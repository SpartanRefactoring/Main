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
        case ASTNode.SWITCH_STATEMENT:
          return $;
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

  static Block getBlock(final ASTNode ¢) {
    return az.block(delimiter(¢));
  }
  
  static SwitchStatement getSwitch(final ASTNode ¢) {
    return az.switchStatement(delimiter(¢));
  }

  /**
   * [[SuppressWarningsSpartan]]
   */
  static Namespace getScopeNamespace(final ASTNode ¢) {
    ASTNode $ = delimiter(¢);
    return new Namespace(Environment.of(iz.block($) ? last(statements(az.block($))) : last(statements(az.switchStatement($)))));
  }

  static String newName(final ASTNode ¢, final Type t) {
    final Block b = getBlock(¢);
    final Namespace n = b.getProperty("Namespace") == null ? getScopeNamespace(b) : (Namespace) ¢.getProperty("Namespace");
    final String $ = n.generateName(t);
    n.addNewName($, t);
    b.setProperty("Namespace", n);
    return $;
  }

  static boolean hasInScope(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢).has(identifier);
  }
}
