package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.ast.safety.iz.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2015-08-23 */
public final class specificity implements Comparator<Expression> {
  /** Determine
   * @param x JD
   * @return whether the parameter has a defined level of specificity. */
  public static boolean defined(final Expression ¢) {
    return Level.defined(¢);
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
        return iz.nullLiteral(¢);
      }
    },
    BOOLEAN {
      @Override boolean includes(final ASTNode ¢) {
        return iz.booleanLiteral(¢);
      }
    },
    LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal(¢);
      }
    },
    CONSTANT {
      @Override boolean includes(final ASTNode ¢) {
        return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) && iz.literal(extract.core(((PrefixExpression) ¢).getOperand()));
      }
    },
    CLASS_CONSTANT {
      @Override boolean includes(final ASTNode ¢) {
        return iz.nodeTypeEquals(¢, SIMPLE_NAME) && ((SimpleName) ¢).getIdentifier().matches("[A-Z_0-9]+");
      }
    },
    THIS {
      @Override boolean includes(final ASTNode ¢) {
        return iz.thisLiteral(¢);
      }
    },
    ZERO_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal0(¢);
      }
    },
    ONE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal1(¢);
      }
    },
    ZERO_DOUBLE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal(¢, 0.0);
      }
    },
    ONE_DOUBLE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return iz.literal(¢, 1.0);
      }
    },
    EMPTY_STRING {
      @Override boolean includes(final ASTNode ¢) {
        return iz.emptyStringLiteral(¢);
      }
    },
    TRUE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return literal.true¢(¢);
      }
    },
    FALSE_LITERAL {
      @Override boolean includes(final ASTNode ¢) {
        return literal.false¢(¢);
      }
    },;
    static boolean defined(final Expression ¢) {
      return of(¢) != values().length;
    }
    static int of(final Expression ¢) {
      return ofCore(extract.core(¢));
    }
    private static int ofCore(final Expression ¢) {
      for (final Level ret : values())
        if (ret.includes(¢))
          return ret.ordinal();
      return values().length;
    }
    abstract boolean includes(ASTNode ¢);
  }
}
