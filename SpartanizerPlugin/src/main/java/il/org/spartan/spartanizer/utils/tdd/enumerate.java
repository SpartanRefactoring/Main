package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since Oct 28, 2016 
 */

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
    if (n == null)
      return 0;
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof Statement)
          ++$.inner;
      }
    });
    return $.inner;
  }

  /** @author Sharon Kuninin
   * @author Yarden Lev
   * @param ¢ the CompilationUnit its methods are counted
   * @return the amount of methods the given CompilationUnit has
   * @since Nov 2, 2016 */
  public static int methods(final CompilationUnit ¢) {
    if (¢ == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    ¢.accept(new ASTVisitor() {
      @Override @SuppressWarnings("unused") public boolean visit(final MethodDeclaration node) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }

  /** @author Ori Marcovitch
   * @param ¢
   * @return */
  public static int methodsWithBody(final ASTNode ¢) {
    if (¢ == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration node) {
        if (step.statements(step.body(node)) != null && !step.statements(step.body(node)).isEmpty())
          ++$.inner;
        return true;
      }
    });
    return $.inner;
  }

  /** see issue #776 for more details
   * @author Yevgenia Shandalov
   * @author Osher Hajaj
   * @since 16-11-07 */
  @SuppressWarnings("boxing") public static int blockTypes(final MethodDeclaration d) {
    int $ = 0;
    final List<?> l = step.statements(step.body(d));
    final boolean[] arr = new boolean[10];
    final int BLOCK = 0, IFSTATE = 1, FORSTATE = 2, WHILESTATE = 3, SWITCHSTATE = 4, DOSTATE = 5, SYNC = 6, TRY = 7, LAMBDA = 7;
    range.to(arr.length).forEach(¢ -> arr[¢] = false);
    for (final Object ¢ : l)
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

  public static int ifStatements(final ASTNode ¢) {
    if (¢ == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final IfStatement __) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }

  public static int loops(final ASTNode ¢) {
    if (¢ == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final WhileStatement __) {
        ++$.inner;
        return true;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final ForStatement __) {
        ++$.inner;
        return true;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final EnhancedForStatement __) {
        ++$.inner;
        return true;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final DoStatement __) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }

  public static int ternaries(final ASTNode ¢) {
    if (¢ == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final ConditionalExpression __) {
        ++$.inner;
        return true;
      }
    });
    return $.inner;
  }
}

