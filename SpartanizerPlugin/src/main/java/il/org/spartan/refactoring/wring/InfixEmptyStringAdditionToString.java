package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.utils.*;

public class InfixEmptyStringAdditionToString extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  static boolean validTypes(final Expression e, final Expression ¢1, final Expression ¢2) {
    System.out.println(e.getAST().hasResolvedBindings());
    System.out.println(¢2 instanceof IVariableBinding);
    System.out.println(((IVariableBinding)¢2).getType().getName());
    return e.getAST().hasResolvedBindings()
        ? (¢2 instanceof IVariableBinding && ¢1 instanceof StringLiteral && ((StringLiteral) ¢1).getEscapedValue().equals("\"\"")
            || ¢2 instanceof StringLiteral && ((StringLiteral) ¢2).getEscapedValue().equals("\"\"") && ¢1 instanceof IVariableBinding)
        : (¢2 instanceof SimpleName && ¢1 instanceof StringLiteral && ((StringLiteral) ¢1).getEscapedValue().equals("\"\"")
            || ¢2 instanceof StringLiteral && ((StringLiteral) ¢2).getEscapedValue().equals("\"\"") && ¢1 instanceof SimpleName);
  }

  // private static ASTNode replacement(final InfixExpression e, final Operator
  // o, final IVariableBinding b, final StringLiteral l) {
  // }
  @Override ASTNode replacement(InfixExpression e) {
    if (!Is.infixPlus(e))
      return null;
    final Expression right = right(e);
    assert right != null;
    final Expression left = left(e);
    assert left != null;
    return !validTypes(e, right, left) ? null : left instanceof IVariableBinding ? left : right;
  }

  private static String descriptionAux(final Expression e) {
    return e == null ? "Use the variable alone" : "Use " + e;
  }

  @Override String description(final InfixExpression e) {
    final Expression right = right(e);
    final Expression left = left(e);
    return descriptionAux(left instanceof IVariableBinding ? left : right);
  }
}
