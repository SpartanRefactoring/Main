package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.navigate.step.condition;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static org.eclipse.jdt.core.dom.ASTNode.ANONYMOUS_CLASS_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.WHILE_STATEMENT;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.safety.iz;

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
