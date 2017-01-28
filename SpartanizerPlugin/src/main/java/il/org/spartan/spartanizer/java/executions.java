package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-28 */
public interface executions {
  enum coupling {
    unknown, together, zeroOrOne;
  }

  static Inner in(final ASTNode ¢) {
    return new Inner(¢);
  }

  class Inner {
    private final ASTNode head;

    public coupling of(final ASTNode start) {
      ASTNode child = start;
      for (final ASTNode parent : ancestors.of(child)) {
        if (start == head) // Final iteration
          return coupling.together;
        if (parent == child) // First iteration
          continue;
        final coupling $ = of(parent, child);
        if ($ != coupling.together)
          return $;
        child = parent;
      }
      throw new IllegalArgumentException();
    }

    private static coupling of(final ASTNode parent, final ASTNode child) {
      switch (parent.getNodeType()) {
        case ASSERT_STATEMENT:
        case CATCH_CLAUSE:
        case SWITCH_CASE:
        case SWITCH_STATEMENT:
        case TRY_STATEMENT:
          return coupling.zeroOrOne;
        case ANNOTATION_TYPE_DECLARATION:
        case ANONYMOUS_CLASS_DECLARATION:
        case DO_STATEMENT:
        case ENUM_DECLARATION:
        case LAMBDA_EXPRESSION:
        case TYPE_DECLARATION:
        case WHILE_STATEMENT:
          return coupling.unknown;
        case BLOCK:
          return ofBlock(az.block(parent), child);
        case CONDITIONAL_EXPRESSION:
          return child == az.conditionalExpression(parent).getExpression() ? coupling.together : coupling.zeroOrOne;
        case ENHANCED_FOR_STATEMENT:
          return child != az.enhancedFor(parent).getExpression() ? coupling.unknown : coupling.together;
        case FOR_STATEMENT:
          return az.forStatement(parent).initializers().contains(child) ? coupling.together : coupling.unknown;
        case IF_STATEMENT:
          return child == az.ifStatement(parent).getExpression() ? coupling.together : coupling.zeroOrOne;
      }
      return coupling.together;
    }

    private static coupling ofBlock(final Block b, final ASTNode child) {
      for (final Statement ¢ : step.statements(b)) {
        if (b == child)
          return coupling.together;
        if (may.exit(¢))
          return coupling.zeroOrOne;
      }
      return coupling.unknown;
    }

    public Inner(final ASTNode ¢) {
      head = ¢;
    }
  }
}
