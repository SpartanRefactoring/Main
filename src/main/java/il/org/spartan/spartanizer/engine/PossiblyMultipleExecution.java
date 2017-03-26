package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** Determines whether a node can be executed multiple times within a given
 * context side of assignments
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-18 */
public class PossiblyMultipleExecution {
  /** Instantiates this class */
  @NotNull public static PossiblyMultipleExecution of(final ASTNode what) {
    return new PossiblyMultipleExecution(what);
  }

  public final ASTNode what;

  private PossiblyMultipleExecution(final ASTNode what) {
    this.what = what;
  }

  public boolean inContext(final ASTNode ¢) {
    return inContext(as.list(¢));
  }

  public boolean inContext(@NotNull final Collection<? extends ASTNode> where) {
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
        case WHILE_STATEMENT:
          return false;
        case FOR_STATEMENT:
          if (multiple((ForStatement) $))
            return false;
          continue;
        case ENHANCED_FOR_STATEMENT:
          if (multiple((EnhancedForStatement) $))
            return false;
      }
    }
    assert fault.unreachable() : fault.specifically("Context does not contain current node", what, where);
    return false;
  }

  private boolean multiple(final EnhancedForStatement $) {
    return touched(body($));
  }

  private boolean multiple(final ForStatement $) {
    return touched(expression($)) || updaters($).stream().anyMatch(λ -> touched(λ));
  }

  private boolean touched(@NotNull final ASTNode n) {
    return descendants.streamOf(n).anyMatch(λ -> λ == what);
  }
}
