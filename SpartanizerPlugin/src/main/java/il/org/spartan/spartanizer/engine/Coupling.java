package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

public enum Coupling {
  DETERMININSTIC, ONEORMORE, UNKNOWN,;
  public static Inner of(final ASTNode ¢) {
    return new Inner(¢);
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

  static class Inner {
    public Inner(final ASTNode n) {}

    public Coupling withRespectTo(final ASTNode to) {
      return null;
    }
  }
}
