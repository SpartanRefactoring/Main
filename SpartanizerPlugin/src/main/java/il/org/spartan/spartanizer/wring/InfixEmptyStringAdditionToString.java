package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

/** transforms "" + x to x when x is of type String
 * @author Stav Namir
 * @since 2016-08-29 */
public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression>
    implements il.org.spartan.spartanizer.wring.Kind.NoImpact {
  @SuppressWarnings("unused") static boolean validTypes(final Expression e, final Expression ¢1, final Expression ¢2) {
    return !stringType.isNot(¢2) && ¢1 instanceof StringLiteral && "\"\"".equals(((StringLiteral) ¢1).getEscapedValue())
        || ¢2 instanceof StringLiteral && "\"\"".equals(((StringLiteral) ¢2).getEscapedValue()) && !stringType.isNot(¢1);
  }

  private static String descriptionAux(final Expression e) {
    return e != null ? "Use " + e : "Use the variable alone";
  }

  @Override public String description() {
    return "Remove \"\" from \"\" + X if X is a String";
  }

  @Override String description(final InfixExpression e) {
    final Expression right = step.right(e);
    final Expression left = step.left(e);
    return descriptionAux(!"\"\"".equals(((StringLiteral) left).getEscapedValue()) ? left : right);
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if (!iz.infixPlus(e))
      return null;
    final Expression right = step.right(e);
    assert right != null;
    final Expression left = step.left(e);
    assert left != null;
    return !validTypes(e, right, left) ? null : !"\"\"".equals(((StringLiteral) left).getEscapedValue()) ? left : right;
  }
}
