package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
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

  @NotNull static List<ASTNode> of(final SingleVariableDeclaration x) {
    @NotNull final List<ASTNode> $ = new ArrayList<>();
    $.add(x);
    return $;
  }

  static List<? extends ASTNode> of(@NotNull final VariableDeclarationFragment ¢) {
    return scope.of(¢.getName());
  }

  @Nullable static List<? extends ASTNode> of(final SimpleName ¢) {
    @Nullable final List<? extends ASTNode> $ = definition.scope(¢);
    assert $ != null : fault.dump() + //
        "\n\t n=" + ¢ + //
        "\n\t definition.kind() = " + definition.kind(¢) + //
        fault.done();
    return $;
  }

  @Nullable static Block getBlock(final ASTNode ¢) {
    return az.block(delimiter(¢));
  }

  /** Bug in ternary spartanizing, do not remove the suppress
   * [[SuppressWarningsSpartan]] */
  @Nullable static Namespace getScopeNamespace(final ASTNode ¢) {
    @Nullable final ASTNode $ = delimiter(¢);
    return new Namespace(Environment.of(last(iz.block($) ? statements(az.block($)) : statements(az.switchStatement($)))));
  }

  @NotNull static String newName(final ASTNode ¢, final Type t) {
    @Nullable final ASTNode b = delimiter(¢);
    @Nullable final Namespace n = b.getProperty("Namespace") == null ? getScopeNamespace(¢) : (Namespace) b.getProperty("Namespace");
    @NotNull final String $ = n.generateName(t);
    n.addNewName($, t);
    b.setProperty("Namespace", n);
    return $;
  }

  @NotNull static String newName(final ASTNode ¢, final Type t, final String s) {
    @Nullable final ASTNode b = delimiter(¢);
    @Nullable final Namespace n = b.getProperty("Namespace") == null ? getScopeNamespace(¢) : (Namespace) b.getProperty("Namespace");
    @NotNull final String $ = n.generateName(s);
    n.addNewName($, t);
    b.setProperty("Namespace", n);
    return $;
  }

  static boolean hasInScope(final ASTNode ¢, final String identifier) {
    return getScopeNamespace(¢).has(identifier);
  }
}
