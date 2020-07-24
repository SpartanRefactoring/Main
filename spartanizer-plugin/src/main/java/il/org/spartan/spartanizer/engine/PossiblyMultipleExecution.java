package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static org.eclipse.jdt.core.dom.ASTNode.ANONYMOUS_CLASS_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.SYNCHRONIZED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.WHILE_STATEMENT;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.utils.fault;

/** Determines whether a node can be executed multiple times within a given
 * context side of assignments
 * @author Yossi Gil
 * @since 2017-03-18 */
public class PossiblyMultipleExecution {
  /** Instantiates this class */
  public static PossiblyMultipleExecution of(final ASTNode what) {
    return new PossiblyMultipleExecution(what);
  }

  public final ASTNode what;

  private PossiblyMultipleExecution(final ASTNode what) {
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
        case ANONYMOUS_CLASS_DECLARATION:
        case DO_STATEMENT:
        case LAMBDA_EXPRESSION:
        case METHOD_DECLARATION:
        case SYNCHRONIZED_STATEMENT:
        case WHILE_STATEMENT:
          return false;
        case ENHANCED_FOR_STATEMENT:
          if (multiple((EnhancedForStatement) $))
            return false;
          break;
        case FOR_STATEMENT:
          if (multiple((ForStatement) $))
            return false;
          continue;
        default:
          break;
      }
    }
    assert fault.unreachable() : fault.specifically("Context does not contain current node", what, where);
    return false;
  }
  private boolean multiple(final EnhancedForStatement $) {
    return touched(body($));
  }
  private boolean multiple(final ForStatement $) {
    return touched(expression($)) || updaters($).stream().anyMatch(this::touched);
  }
  private boolean touched(final ASTNode n) {
    return descendants.streamOf(n).anyMatch(λ -> λ == what);
  }
}
