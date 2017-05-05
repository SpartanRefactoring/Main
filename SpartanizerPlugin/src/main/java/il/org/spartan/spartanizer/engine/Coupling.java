package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

public enum Coupling {
  IFF, IMPLIED, INDEPENDENT,;
  @SuppressWarnings("unused") public static Inner of(final ASTNode next) {
    return λ -> null;
  }
  public static boolean unknownNumberOfEvaluations(final ASTNode n, final Statement s) {
    ASTNode child = n;
    for (final ASTNode ancestor : hop.ancestors(n)) {
      if (s == n)
        break;
      if (iz.nodeTypeIn(ancestor, WHILE_STATEMENT, DO_STATEMENT, ANONYMOUS_CLASS_DECLARATION, LAMBDA_EXPRESSION))
        return true;
      if (iz.expressionOfEnhancedFor(child, ancestor))
        continue;
      if (iz.nodeTypeEquals(ancestor, FOR_STATEMENT) && (yieldAncestors.untilOneOf(updaters((ForStatement) ancestor)).inclusiveFrom(child) != null
          || yieldAncestors.untilNode(condition((ForStatement) ancestor)).inclusiveFrom(child) != null))
        return true;
      child = ancestor;
    }
    return false;
  }

  @FunctionalInterface
  interface Inner {
    Coupling withRespectTo(ASTNode to);
  }
}
