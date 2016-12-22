package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Yossi Gil
 * @since 2016-12 */
public interface scope {
  int a = 0, b = 0;

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

  static List<? extends ASTNode> of(SimpleName x) {
    return definition.kind(x).scope(x);
  }
}
