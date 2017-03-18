package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;

/**
 * TODO Yossi Gil: document class 
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-18
 */
public class PotentialMultipleExecution {
  public final ASTNode what;
  public static PotentialMultipleExecution of(final ASTNode what) {
    return new PotentialMultipleExecution(what);
    
  }

  private PotentialMultipleExecution(final ASTNode what) {
    this.what = what;
  }

  public boolean inParent(final ASTNode parent) {
    return !iz.nodeTypeIn(parent, ANONYMOUS_CLASS_DECLARATION, TRY_STATEMENT, SYNCHRONIZED_STATEMENT, LAMBDA_EXPRESSION, WHILE_STATEMENT, DO_STATEMENT)
        && (parent instanceof ForStatement ? inParent((ForStatement) parent) : //
            parent instanceof EnhancedForStatement && inParent( (EnhancedForStatement) parent));
  }

  public boolean inParent(final EnhancedForStatement s) {
    return descendants.of(s.getExpression()).contains(what);
  }

  public boolean inParent(final ForStatement s) {
    return initializers(s).stream().anyMatch(a -> descendants.of(a).contains(what));
  }


  public boolean never(final Statement s) {
    return system.stream(yieldAncestors.until(s).ancestors(what)).anyMatch(λ -> !inParent( λ));
  }

  /** Determines whether a specific SimpleName was used in a
   * {@link ForStatement}.
   * @param s JD
   * @param n JD
   * @return whether the SimpleName is used in a ForStatement's condition,
   *         updaters, or body. */
  public boolean variableUsedInFor(final ForStatement s) {
    return !collect.usesOf((SimpleName)what).in(condition(s), body(s)).isEmpty() || !collect.usesOf(n).in(updaters(s)).isEmpty();
  }

  public static boolean unknownNumberOfEvaluations(final ASTNode what, final ASTNode context) {
    return of(what).inContext(context);
  }
  public boolean inContext(final ASTNode context) {
    for (ASTNode current = what.getParent(); current != null; current =current.getParent())  {
      if (current == context)
        return false;
      if (iz.nodeTypeIn(current, WHILE_STATEMENT, DO_STATEMENT, ANONYMOUS_CLASS_DECLARATION, LAMBDA_EXPRESSION))
        return true;
      if (iz.expressionOfEnhancedFor(what, ancestor))
        continue;
      if (iz.nodeTypeEquals(ancestor, FOR_STATEMENT) && (yieldAncestors.untilOneOf(updaters((ForStatement) ancestor)).inclusiveFrom(child) != null
          || yieldAncestors.untilNode(condition((ForStatement) ancestor)).inclusiveFrom(child) != null))
        return true;
      child = ancestor;
    }
    return false;
  }
   
}
