package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016-10-07 */
public interface dig {
  static List<String> stringLiterals(final ASTNode n) {
    final List<String> ret = an.empty.list();
    if (n == null)
      return ret;
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final StringLiteral ¢) {
        ret.add(¢.getLiteralValue());
        return true;
      }
    });
    return ret;
  }
}
