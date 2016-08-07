package il.org.spartan.refactoring.utils;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;

/** @author Yossi Gil
 * @since 2015-08-23 */
public class Specificity implements Comparator<Expression> {
  /** A comparison of two {@link Expression} by their level of specificity.
   * @param e1 JD
   * @param e2 JD
   * @return a negative, zero, or positive integer, depending on the level of
   *         specificity the first parameter, is less than, equal, or greater
   *         than the specificity level of the second parameter. */
  @Override public int compare(final Expression e1, final Expression e2) {
    return Level.of(e1) - Level.of(e2);
  }
  /** Determine
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter has a defined
   *         level of specificity. */
  public static boolean defined(final Expression e) {
    return Level.defined(e);
  }

  enum Level {
    NULL {
      @Override boolean includes(final ASTNode n) {
        return Is.null_(n);
      }
    },
    BOOLEAN_LITERAL {
      @Override boolean includes(final ASTNode n) {
        return Is.booleanLiteral(n);
      }
    },
    LITERAL {
      @Override boolean includes(final ASTNode n) {
        return Is.literal(n);
      }
    },
    CONSTANT {
      @Override boolean includes(final ASTNode n) {
        return n.getNodeType() == PREFIX_EXPRESSION && Is.literal(extract.core(((PrefixExpression) n).getOperand()));
      }
    },
    CLASS_CONSTANT {
      @Override boolean includes(final ASTNode n) {
        return n.getNodeType() == SIMPLE_NAME && ((SimpleName) n).getIdentifier().matches("[A-Z_0-9]+");
      }
    },
    THIS {
      @Override boolean includes(final ASTNode n) {
        return Is.this_(n);
      }
    },;
    static int of(final ASTNode n) {
      final Expression e = extract.core((Expression) n);
      for (final Level l : values())
        if (l.includes(e))
          return l.ordinal();
      return values().length;
    }
    static boolean defined(final Expression e) {
      return of(e) != values().length;
    }
    abstract boolean includes(final ASTNode n);
  }
}
