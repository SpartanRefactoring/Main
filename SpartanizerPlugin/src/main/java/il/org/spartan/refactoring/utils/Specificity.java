package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Yossi Gil
 * @since 2015-08-23 */
public class Specificity implements Comparator<Expression> {
  /** Determine
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter has a defined
   *         level of specificity. */
  public static boolean defined(final Expression e) {
    return Level.defined(e);
  }

  /** A comparison of two {@link Expression} by their level of specificity.
   * @param e1 JD
   * @param e2 JD
   * @return a negative, zero, or positive integer, depending on the level of
   *         specificity the first parameter, is less than, equal, or greater
   *         than the specificity level of the second parameter. */
  @Override public int compare(final Expression e1, final Expression e2) {
    return Level.of(e1) - Level.of(e2);
  }

  enum Level {
    NULL {
      @Override boolean includes(final ASTNode n) {
        return Is.null_(n);
      }
    },
    BOOLEAN {
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
    },
    ZERO_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(asNumberLiteral(¢), 0);
      }
    },
    ONE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(asNumberLiteral(¢), 1);
      }
    },
    ZERO_DOUBLE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(¢, 0.0);
      }
    },
    ONE_DOUBLE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(¢, 1.0);
      }
    },
    EMPTY_STRING {
      @Override boolean includes(final ASTNode ¢) {
        return isEmptyStringLiteral(¢);
      }
    },
    TRUE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteralTrue(¢);
      }
    },
    FALSE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return isLiteralFalse(¢);
      }
    },;
    static boolean defined(final Expression e) {
      return of(e) != values().length;
    }

    static int of(final ASTNode n) {
      final Expression e = extract.core((Expression) n);
      for (final Level l : values())
        if (l.includes(e))
          return l.ordinal();
      return values().length;
    }

    abstract boolean includes(final ASTNode ¢);
  }
}
