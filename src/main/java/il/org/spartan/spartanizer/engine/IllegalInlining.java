package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** Determines whether an inlining spot is valid. Invalid sports are left hand
 * side of assignments
 * @author Yossi Gil {@code Yossi.Gil@GMail.com}
 * @since 2017-03-18 */
public class IllegalInlining {
  /** Instantiates this class */
  public static IllegalInlining of(final ASTNode what) {
    return new IllegalInlining(what);
  }

  public final ASTNode what;

  private IllegalInlining(final ASTNode what) {
    this.what = what;
  }

  public boolean inContext(final ASTNode ¢) {
    return inContext(as.list(¢));
  }

  public boolean inContext(final Collection<? extends ASTNode> where) {
    for (ASTNode $ = what.getParent(); $ != null; $ = $.getParent()) {
      if (where.contains($))
        return false;
      switch ($.getNodeType()) {
        default:
          continue;
        case ANONYMOUS_CLASS_DECLARATION:
        case DO_STATEMENT:
        case LAMBDA_EXPRESSION:
        case METHOD_DECLARATION:
        case SYNCHRONIZED_STATEMENT:
        case TRY_STATEMENT:
        case WHILE_STATEMENT:
          return false;
        case FOR_STATEMENT:
          return !initializers((ForStatement) $).stream().anyMatch(λ -> descendants.of(λ).contains(what));
        case ENHANCED_FOR_STATEMENT:
          return !descendants.of(((EnhancedForStatement) $).getExpression()).contains(what);
      }
    }
    assert fault.unreachable() : fault.specifically("Context does not contain current node", what, where);
    return false;
  }



  public static boolean unknownNumberOfEvaluations(ASTNode parent, ASTNode context) {
    return of(parent).inContext(context);
  }
}
