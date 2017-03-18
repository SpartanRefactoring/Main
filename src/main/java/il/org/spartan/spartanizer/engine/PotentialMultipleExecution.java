package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** Determines whether a node might be executed more than once in a given
 * context
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-18 */
public class PotentialMultipleExecution {
  /** Instantiates this class */
  public static PotentialMultipleExecution of(final ASTNode what) {
    return new PotentialMultipleExecution(what);
  }

  public static boolean unknownNumberOfEvaluations(final ASTNode what, final ASTNode context) {
    return of(what).inContext(context);
  }

  public final ASTNode what;

  private PotentialMultipleExecution(final ASTNode what) {
    this.what = what;
  }

  public boolean inContext(final ASTNode context) {
    for (ASTNode current = what.getParent(); current != null; current = current.getParent()) {
      if (current == context)
        return false;
      switch (current.getNodeType()) {
        default:
          continue;
        case METHOD_DECLARATION:
        case ANONYMOUS_CLASS_DECLARATION:
        case DO_STATEMENT:
        case LAMBDA_EXPRESSION:
        case SYNCHRONIZED_STATEMENT: 
        case TRY_STATEMENT:
        case WHILE_STATEMENT:
          return false;
        case FOR_STATEMENT:
          return !initializers((ForStatement) current).stream().anyMatch(a -> descendants.of(a).contains(what));
        case ENHANCED_FOR_STATEMENT:
          return !descendants.of(((EnhancedForStatement) current).getExpression()).contains(what);
      }
    }
    assert fault.unreachable() : fault.specifically("Context does not contain current node", what, context);
    return false;
  }

  public static boolean unknownNumberOfEvaluations(final MethodDeclaration d) {
    final Block $ = body(d);
    return $ != null && statements($).stream().anyMatch(λ -> unknownNumberOfEvaluations(d, λ));
  }
}
