package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
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
    final Int $ = new Int();
    $.inner = 0;
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.expression(¢))
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** @author Aviad Cohen
   * @author Noam Yefet
   * @param ¢
   * @since Nov 1, 2016 */
  public static int statements(final ASTNode n) {
    final Int count = new Int();
    if (n == null)
      return 0;
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement)
          ++count.inner;
      }
    });
    return count.inner;
  }

  /** @author Sharon Kuninin
   * @author Yarden Lev
   * @param ¢ the CompilationUnit its methods are counted
   * @return the amount of methods the given CompilationUnit has
   * @since Nov 2, 2016 */
  public static int methods(final CompilationUnit ¢) {
    if (¢ == null)
      return 0;
    final Int counter = new Int();
    counter.inner = 0;
    ¢.accept(new ASTVisitor() {
      @SuppressWarnings("unused") @Override public boolean visit(final MethodDeclaration node) {
        ++counter.inner;
        return true;
      }
    });
    return counter.inner;
  }

  /** see issue #776 for more details
   * @author Yevgenia Shandalov
   * @author Osher Hajaj
   * @since 16-11-03 */
  public static int blockTypes(MethodDeclaration d) {
    int $ = 0;
    List l = d.getBody().statements();
    boolean[] arr = new boolean[15];
    for (int ¢ = 0; ¢ < arr.length; ++¢)
      arr[¢] = false;
    for (Object ¢ : l)
      if (¢ instanceof Block && !arr[0]) {
        ++$;
        arr[0] = true;
      } else if (¢ instanceof IfStatement && !arr[1]) {
        ++$;
        arr[1] = true;
      } else if (¢ instanceof ForStatement && !arr[2]) {
        ++$;
        arr[2] = true;
      } else if (¢ instanceof WhileStatement && !arr[3]) {
        ++$;
        arr[3] = true;
      }
    
    return $;
  }
  // For you to implement! Let's TDD and get it on!
}
