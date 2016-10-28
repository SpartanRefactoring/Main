package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Oct 28, 2016 */
public interface count {
  /** @author Ori Marcovitch
   * @param astNode
   * @since Oct 28, 2016 */
  static int expressions(ASTNode n) {
    if (n == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    n.accept(new ASTVisitor() {
      @Override public boolean visit(SimpleName node) {
        ++$.inner;
        return true;
      }

      @Override public boolean visit(NullLiteral node) {
        ++$.inner;
        return true;
      }

      @Override public boolean visit(MethodInvocation node) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }
}
