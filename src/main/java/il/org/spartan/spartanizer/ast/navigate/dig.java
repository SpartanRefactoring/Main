package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-10-07 */
public interface dig {
  static List<String> stringLiterals(final ASTNode n) {
    final List<String> $ = new ArrayList<>();
    if (n == null)
      return $;
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final StringLiteral ¢) {
        $.add(¢.getLiteralValue());
        return true;
      }
    });
    return $;
  }
}
