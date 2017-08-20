package il.org.spartan.spartanizer.ast.factory;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.analyses.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase
 * @author Yossi Gil
 * @since 2017-08-18 */
public interface atomic {
  static SimpleName newCent(final ASTNode ¢) {
    return make.from(¢).identifier(notation.cent);
  }
  static NullLiteral nullLiteral(final ASTNode ¢) {
    return make.from(¢).nullLiteral();
  }
  static EmptyStatement emptyStatement(final ASTNode ¢) {
    return ¢.getAST().newEmptyStatement();
  }
  static NumberLiteral newLiteral(final ASTNode n, final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }
}
