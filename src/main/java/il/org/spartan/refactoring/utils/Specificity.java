package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.extract.*;
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
      @Override boolean includes(final ASTNode ¢) {
        return Is.null_(¢);
      }
    },
    LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return Is.literal(¢);
      }
    },
    CONSTANT {
      @Override boolean includes(final ASTNode ¢) {
        return Is.literal(operand(asPrefixExpression(¢)));
      }
    },
    CLASS_CONSTANT {
      @Override boolean includes(final ASTNode ¢) {
        return ¢.getNodeType() == SIMPLE_NAME && ((SimpleName) ¢).getIdentifier().matches("[A-Z_0-9]+");
      }
    },
    THIS{
      @Override boolean includes(final ASTNode ¢) {
        return Is.this_(¢);
      }
    }, ZERO_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(asNumberLiteral(¢), 0);
      }
    }, ONE_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(asNumberLiteral(¢), 1);
      }
    }, ZERO_DOUBLE_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral((¢), 0.0);
      }
    }, ONE_DOUBLE_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral((¢), 1.0);
      }
    }, EMPTY_STRING{
      @Override boolean includes(final ASTNode ¢) {
        return isEmptyStringLiteral(¢);
      }
    }, TRUE_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(¢, true);
      }
    }, FALSE_LITERAL{
      @Override boolean includes(final ASTNode ¢) {
        return isLiteral(¢, false);
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
