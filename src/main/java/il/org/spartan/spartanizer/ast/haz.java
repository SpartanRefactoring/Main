package il.org.spartan.spartanizer.ast;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016-09-12 */
public enum haz {
  ;
  public static boolean dollar(final List<SimpleName> ns) {
    for (final SimpleName ¢ : ns)
      if ("$".equals(identifier(¢)))
        return true;
    return false;
  }

  @SuppressWarnings("unused") public static boolean variableDefinition(final ASTNode n) {
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.FALSE);
    n.accept(new ASTVisitor() {
      boolean found() {
        $.set(Boolean.TRUE);
        return false;
      }

      @Override public boolean visit(final EnumConstantDeclaration __) {
        return found();
      }

      @Override public boolean visit(final FieldDeclaration node) {
        return found();
      }

      @Override public boolean visit(final SingleVariableDeclaration node) {
        return found();
      }

      @Override public boolean visit(final VariableDeclarationExpression node) {
        return found();
      }

      @Override public boolean visit(final VariableDeclarationFragment __) {
        return found();
      }

      @Override public boolean visit(final VariableDeclarationStatement __) {
        return found();
      }
    });
    return $.get().booleanValue();
  }
}
