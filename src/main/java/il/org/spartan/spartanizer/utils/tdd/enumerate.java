package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Oct 28, 2016 */
public enum enumerate {
  ;
  /** @author Ori Marcovitch
   * @param n node
   * @since Oct 28, 2016 */
  public static int expressions(final ASTNode n) {
    if (n == null)
      return 0;
    final Int x = new Int();
    x.inner = 0;
    n.accept(new ASTVisitor() {
      @Override public void preVisit(ASTNode ¢) {
        if (¢ instanceof Expression)
          ++x.inner;
      }
    });
    return x.inner;
  }
  // For you to implement! Let's TDD and get it on!
}
