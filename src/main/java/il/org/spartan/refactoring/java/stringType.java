package il.org.spartan.refactoring.java;

import static il.org.spartan.refactoring.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.java.PrudentType.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.ast.*;

/** @author Yossi Gil
 * @since 2016 */
public enum stringType {
  ;
  /** @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is an expression
   *         whose type is provably not of type {@link String}, in the sense
   *         used in applying the <code>+</code> operator to concatenate
   *         strings. concatenation. */
  public static boolean isNot(final Expression x) {
    return stringType.isNotFromContext(x) || !in(prudent(x), STRING, ALPHANUMERIC)/*stringType.isNotFromStructure(az.infixExpression(x))*/;
  }

  /** Determine whether a <i>all</i> elements list of {@link Expression} are
   * provably not a string.
   * @param xs JD
   * @return <code><b>true</b></code> <i>iff</i> all elements in the argument
   *         are provably not a {@link String}.
   * @see stringType#isNot(Expression) */
  private static boolean areNot(final List<Expression> xs) {
    for (final Expression e : xs)
      if (!stringType.isNotFromStructure(e))
        return false;
    return true;
  }

  private static boolean isNotFromContext(final Expression x) {
    for (ASTNode context = parent(x); context != null; context = parent(context))
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

  /** Determine whether an {@link Expression} could not be evaluated as a
   * string.
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is not a string
   *         or composed of appended strings */
  private static boolean isNotFromStructure(final Expression x) {
    return notStringSelf(x) || isNotFromStructure(az.infixExpression(x));
  }

  private static boolean isNotFromStructure(final InfixExpression x) {
    return x != null && (x.getOperator() != PLUS || areNot(extract.allOperands(x)));
  }

  @SuppressWarnings("boxing") private static boolean notStringSelf(final Expression x) {
    return in(x.getNodeType(), ARRAY_CREATION, BOOLEAN_LITERAL, CHARACTER_LITERAL, INSTANCEOF_EXPRESSION, //
        NULL_LITERAL, NUMBER_LITERAL, PREFIX_EXPRESSION);
  }
}
