package il.org.spartan.spartanizer.java;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.java.PrudentType.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

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
    return stringType.isNotFromContext(x) || !in(prudent(x), STRING, ALPHANUMERIC);
  }

  private static boolean isNotFromContext(final Expression x) {
    for (ASTNode context = parent(x); context != null; context = parent(context))
      switch (context.getNodeType()) {
        case INFIX_EXPRESSION:
          if (((InfixExpression) context).getOperator().equals(PLUS))
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
}
