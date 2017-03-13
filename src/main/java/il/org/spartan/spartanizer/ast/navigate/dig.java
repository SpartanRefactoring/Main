package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-10-07 */
public interface dig {
  static List<String> stringLiterals(final ASTNode n) {
    final List<String> $ = new ArrayList<>();
    if (n == null)
      return $;
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final StringLiteral ¢) {
        $.add(¢.getLiteralValue());
        return true;
      }
    });
    return $;
  }
}
