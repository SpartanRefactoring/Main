package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** @author Yossi Gil
 * @since 2015-08-23 */
public class specificity implements Comparator<Expression> {
  /** Determine
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter has a defined
   *         level of specificity. */
  public static boolean defined(final Expression x) {
    return Level.defined(x);
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
        return iz.nullLiteral(n);
      }
    },
    BOOLEAN {
      @Override boolean includes(final ASTNode n) {
        return iz.booleanLiteral(n);
      }
    },
    LITERAL {
      @Override boolean includes(final ASTNode n) {
        return iz.literal(n);
      }
    },
    CONSTANT {
      @Override boolean includes(final ASTNode n) {
        return iz.is(n, PREFIX__EXPRESSION) && iz.literal(extract.core(((PrefixExpression) n).getOperand()));
      }
    },
    CLASS__CONSTANT {
      @Override boolean includes(final ASTNode n) {
        return iz.is(n, SIMPLE__NAME) && ((SimpleName) n).getIdentifier().matches("[A-Z__0-9]+");
      }
    },
    THIS {
      @Override boolean includes(final ASTNode n) {
        return iz.thisLiteral(n);
      }
    },
    ZERO__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        final NumberLiteral ¢1 = az.numberLiteral(¢);
        return ¢1 != null && iz.literal(¢1.getToken(), 0);
      }
    },
    ONE__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        final NumberLiteral ¢1 = az.numberLiteral(¢);
        return ¢1 != null && iz.literal(¢1.getToken(), 1);
      }
    },
    ZERO__DOUBLE__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal(¢, 0.0);
      }
    },
    ONE__DOUBLE__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal(¢, 1.0);
      }
    },
    EMPTY__STRING {
      @Override boolean includes(final ASTNode ¢) {
        return iz.emptyStringLiteral(¢);
      }
    },
    TRUE__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literalTrue(¢);
      }
    },
    FALSE__LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literalFalse(¢);
      }
    },;
    static boolean defined(final Expression x) {
      return of(x) != values().length;
    }

    static int of(final Expression ¢) {
      return ofCore(extract.core(¢));
    }

    private static int ofCore(final Expression ¢) {
      for (final Level $ : values())
        if ($.includes(¢))
          return $.ordinal();
      return values().length;
    }

    abstract boolean includes(final ASTNode ¢);
  }
}
