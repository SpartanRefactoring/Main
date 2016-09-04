package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

/** transforms "" + x to x when x is of type String
 * @author Stav Namir
 * @since 2016-08-29 */
public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression>
    implements il.org.spartan.refactoring.wring.Kind.NoImpact {
  private static String descriptionAux(final Expression x) {
    return x != null ? "Use " + x : "Use the variable alone";
  }

  @SuppressWarnings("unused") static boolean validTypes(final Expression x, final Expression ¢1, final Expression ¢2) {
    return !stringType.isNot(¢2) && ¢1 instanceof StringLiteral && "\"\"".equals(((StringLiteral) ¢1).getEscapedValue())
        || ¢2 instanceof StringLiteral && "\"\"".equals(((StringLiteral) ¢2).getEscapedValue()) && !stringType.isNot(¢1);
  }

  @Override public String description() {
    return "Remove \"\" from \"\" + X if X is a String";
  }

  @Override String description(final InfixExpression x) {
    final Expression right = step.right(x);
    final Expression left = step.left(x);
    return descriptionAux(!"\"\"".equals(((StringLiteral) left).getEscapedValue()) ? left : right);
  }

  @Override ASTNode replacement(final InfixExpression x) {
    if (!iz.infixPlus(x))
      return null;
    final Expression right = step.right(x);
    assert right != null;
    final Expression left = step.left(x);
    assert left != null;
    return !validTypes(x, right, left) ? null : !"\"\"".equals(((StringLiteral) left).getEscapedValue()) ? left : right;
  }
}
