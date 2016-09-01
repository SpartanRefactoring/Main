package il.org.spartan.refactoring.java;

import static il.org.spartan.refactoring.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;

/** @author Yossi Gil
 * @since 2016 */
public enum stringType {
  ;
  /** @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose type is provably not of type {@link String}, in the sense
   *         used in applying the <code>+</code> operator to concatenate
   *         strings. concatenation. */
  public static boolean isNot(final Expression e) {
    return stringType.notStringSelf(e) || stringType.isNotFromContext(e) || stringType.isNotFromStructure(az.infixExpression(e));
  }

  private static boolean isNotFromContext(final Expression e) {
    for (ASTNode context = parent(e); context != null; context = parent(context))
      switch (context.getNodeType()) {
        case INFIX_EXPRESSION:
          if (az.infixExpression(context).getOperator().equals(PLUS))
            continue;
          return true;
        case ARRAY_ACCESS:
        case PREFIX_EXPRESSION:
        case POSTFIX_EXPRESSION:
          return true;
        case PARENTHESIZED_EXPRESSION:
          continue;
        default:
          return false;
      }
    return false;
  }

  private static boolean isNotFromStructure(final InfixExpression e) {
    return e != null && (e.getOperator() != PLUS || areNot(extract.allOperands(e)));
  }

  private static boolean notStringSelf(final Expression e) {
    final int[] is = { ARRAY_CREATION, BOOLEAN_LITERAL, CHARACTER_LITERAL, INSTANCEOF_EXPRESSION, NULL_LITERAL, NUMBER_LITERAL, PREFIX_EXPRESSION };
    for (final int ¢ : is)
      if (¢ == e.getNodeType())
        return true;
    return false;
  }
}
