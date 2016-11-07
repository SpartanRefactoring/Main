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
   * @since 16-11-07 */
  public static int blockTypes(MethodDeclaration d) {
    int $ = 0;
    List<?> l = d.getBody().statements();
    boolean[] arr = new boolean[10];
    final int BLOCK = 0;
    final int IFSTATE = 1;
    final int FORSTATE = 2;
    final int WHILESTATE = 3;
    final int SWITCHSTATE = 4;
    final int DOSTATE = 5;
    final int SYNC = 6;
    final int TRY = 7;
    final int LAMBDA = 7;
    // TODO: deal with lambada-expr
    // d.accept(new ASTVisitor() {
    //
    // });
    for (int ¢ = 0; ¢ < arr.length; ++¢)
      arr[¢] = false;
    for (Object ¢ : l)
      if (¢ instanceof Block && !arr[BLOCK]) {
        ++$;
        arr[BLOCK] = true;
      } else if (¢ instanceof IfStatement && !arr[IFSTATE] && (¢ + "").contains("{")) {
        ++$;
        arr[IFSTATE] = true;
      } else if ((¢ instanceof ForStatement || ¢ instanceof EnhancedForStatement) && !arr[FORSTATE] && (¢ + "").contains("{")) {
        ++$;
        arr[FORSTATE] = true;
      } else if (¢ instanceof WhileStatement && !arr[WHILESTATE] && (¢ + "").contains("{")) {
        ++$;
        arr[WHILESTATE] = true;
      } else if ((¢ instanceof SwitchStatement || ¢ instanceof SwitchCase) && !arr[SWITCHSTATE]) {
        ++$;
        arr[SWITCHSTATE] = true;
      } else if (¢ instanceof DoStatement && !arr[DOSTATE]) {
        ++$;
        arr[DOSTATE] = true;
      } else if (¢ instanceof SynchronizedStatement && !arr[SYNC] && (¢ + "").contains("{")) {
        ++$;
        arr[SYNC] = true;
      } else if (¢ instanceof TryStatement && !arr[TRY] && (¢ + "").contains("{")) {
        ++$;
        arr[TRY] = true;
      } else if (¢ instanceof VariableDeclarationStatement && !arr[LAMBDA] && ((¢ + "").contains("-> {") || (¢ + "").contains("->{"))) {
        ++$;
        arr[LAMBDA] = true;
      }
    return $;
  }
  // For you to implement! Let's TDD and get it on!
}
