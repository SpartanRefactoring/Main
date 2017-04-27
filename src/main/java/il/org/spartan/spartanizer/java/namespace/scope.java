package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-12 */
public interface scope {
  static ASTNode delimiter(final ASTNode ¢) {
    if (iz.nodeTypeIn(¢, BLOCK, SWITCH_STATEMENT))
      return ¢;
    for (final ASTNode $ : ancestors.of(¢))
      if (iz.nodeTypeIn($, BLOCK, SWITCH_STATEMENT))
        return $;
    return null;
  }

  static List<ASTNode> of(final SingleVariableDeclaration x) {
    final List<ASTNode> $ = an.empty.list();
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

  static Namespace getScopeNamespace(final ASTNode ¢) {
    final ASTNode delimiter = delimiter(¢);
    if(delimiter == null)
      return null;
    if(delimiter.getProperty("Namespace") != null)
      return (Namespace) delimiter.getProperty("Namespace");
    final List<Statement> statements = statements(delimiter);
    final Statement last = the.lastOf(statements);
    Namespace of = Environment.of(last);
    if(of == null)
      of = new Namespace(of);
    delimiter.setProperty("Namespace", of);
    return of;
  }
  
  static String newName(final ASTNode ¢, final Type t) {
    final Namespace n = getScopeNamespace(¢);
    final String $ = n.generateName(t);
    n.addNewName($, t);
    return $;
  }

  static String newName(final ASTNode ¢, final Type t, final String s) {
    final Namespace n = getScopeNamespace(¢);
    final String $ = n.generateName(s);
    n.addNewName($, t);
    return $;
  }
  
  /** returns whether identifier exists in the environment (does not include nested scopes) */
  static boolean hasInScope(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢) != null && getScopeNamespace(¢).has(identifier);
  }
  
  /** returns whether identifier exists in the environment (includes nested scopes) */
  static boolean hasInScopeComplex(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢).hasComplex(identifier);
  }
}
