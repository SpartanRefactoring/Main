package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression>
    implements il.org.spartan.refactoring.wring.Kind.NoImpact {
  static boolean validTypes(final Expression e, final Expression ¢1, final Expression ¢2) {
    return ¢2 instanceof StringLiteral && ¢1 instanceof StringLiteral && ((StringLiteral) ¢1).getEscapedValue().equals("\"\"")
        || ¢2 instanceof StringLiteral && ((StringLiteral) ¢2).getEscapedValue().equals("\"\"") && ¢1 instanceof StringLiteral;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    if (!iz.infixPlus(e))
      return null;
    final Expression right = right(e);
    assert right != null;
    final Expression left = left(e);
    assert left != null;
    return !validTypes(e, right, left) ? null : !((StringLiteral) left).getEscapedValue().equals("\"\"") ? left : right;
  }

  private static String descriptionAux(final Expression e) {
    return e == null ? "Use the variable alone" : "Use " + e;
  }

  @Override String description(final InfixExpression e) {
    final Expression right = right(e);
    final Expression left = left(e);
    return descriptionAux(!((StringLiteral) left).getEscapedValue().equals("\"\"") ? left : right);
  }

  @Override public String description() {
    return "Remove \"\" from \"\" + X if X is a String";
  }
}
