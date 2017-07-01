package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.utils.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-12 */
public interface scope {
  static List<ASTNode> of(final SingleVariableDeclaration x) {
    final List<ASTNode> ret = an.empty.list();
    ret.add(x);
    return ret;
  }
  static List<? extends ASTNode> of(final VariableDeclarationFragment ¢) {
    return scope.of(¢.getName());
  }
  static List<? extends ASTNode> of(final SimpleName ¢) {
    final List<? extends ASTNode> ret = definition.scope(¢);
    assert ret != null : fault.dump() + //
        "\n\t n=" + ¢ + //
        "\n\t definition.kind() = " + definition.kind(¢) + //
        fault.done();
    return ret;
  }
  static void initializeNamespaces(final ASTNode ¢) {
    final ASTNode root = ¢.getRoot();
    if (root.getProperty("Namespace") != null)
      return;
    Namespace $ = Environment.of(root);
    if ($ == null)
      $ = new Namespace($);
  }
  static Namespace getScopeNamespace(final ASTNode ¢) {
    Namespace ret = Environment.of(¢);
    if (ret != null || (ret = (Namespace) ¢.getRoot().getProperty("Namspace")) != null)
      return ret;
    ret = new Namespace(null);
    ¢.getRoot().setProperty("Namspace", ret);
    return ret;
  }
  static String newName(final ASTNode ¢, final Type t) {
    final Namespace n = getScopeNamespace(¢);
    final String ret = n.generateName(t);
    n.addNewName(ret, t);
    return ret;
  }
  static String newName(final ASTNode ¢, final Type t, final String s) {
    final Namespace n = getScopeNamespace(¢);
    final String ret = n.generateName(s);
    n.addNewName(ret, t);
    return ret;
  }
  /** returns whether identifier exists in the environment (does not include
   * nested scopes) */
  static boolean hasInScope(final ASTNode ¢, final String identifier) {
    return ¢ != null && ¢.getRoot() != null && getScopeNamespace(¢) != null && getScopeNamespace(¢).has(identifier);
  }
  /** returns whether identifier exists in the environment (includes nested
   * scopes) */
  static boolean hasInScopeComplex(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢).hasComplex(identifier);
  }
}
