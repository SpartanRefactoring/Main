package il.org.spartan.refactoring.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;

/** Various methods for comparing
 * @author Yossi Gil
 * @since 2015-07-19 */
public enum ExpressionComparator implements Comparator<Expression> {
  /** Order on terms in addition: literals must be last. Sort literals by
   * length.
   * @author Yossi Gil
   * @since 2015-07-19 */
  ADDITION {
    @Override public int compare(final Expression e1, final Expression e2) {
      int $;
      return ($ = literalCompare(e1, e2)) != 0 || ($ = nodesCompare(e1, e2)) != 0 || ($ = characterCompare(e1, e2)) != 0
          || ($ = alphabeticalCompare(e1, e2)) != 0 ? $ : 0;
    }
  },
  /** Order on terms in addition: except that we do not sort alphabetically
   * @author Yossi Gil
   * @since 2015-07-19 */
  PRUDENT {
    @Override public int compare(final Expression e1, final Expression e2) {
      int $;
      return ($ = literalCompare(e1, e2)) != 0 || ($ = nodesCompare(e1, e2)) != 0 || ($ = characterCompare(e1, e2)) != 0 ? $ : 0;
    }
  },
  /** Order on terms in multiplication: literals must be last. Sort literals by
   * length.
   * @author Yossi Gil
   * @since 2015-07-19 */
  MULTIPLICATION {
    @Override public int compare(final Expression e1, final Expression e2) {
      int $;
      return ($ = literalCompare(e2, e1)) == 0 && ($ = nodesCompare(e1, e2)) == 0 && ($ = characterCompare(e1, e2)) == 0
          && ($ = alphabeticalCompare(e1, e2)) == 0 ? 0 : $;
    }
  };
  private static specificity specificity = new specificity();
  /** Threshold for comparing nodes; a difference in the number of nodes between
   * two nodes is considered zero, if it is the less than this value, */
  public static final int NODES_THRESHOLD = 1;

  /** Lexicographical comparison expressions by their number of characters
   * @param e1 JD
   * @param e2 JD
   * @return an integer which is either negative, zero, or positive, if the
   *         number of characters in the first argument occurs before, at the
   *         same place, or after then the second argument in lexicographical
   *         order. */
  static int alphabeticalCompare(final Expression e1, final Expression e2) {
    return removeWhites(wizard.body(e1)).compareTo(removeWhites(wizard.body(e2)));
  }

  static int argumentsCompare(final Expression e1, final Expression e2) {
    return !iz.methodInvocation(e1) || !iz.methodInvocation(e2) ? 0 : argumentsCompare((MethodInvocation) e1, (MethodInvocation) e2);
  }

  static int argumentsCompare(final MethodInvocation i1, final MethodInvocation i2) {
    return arguments(i1).size() - arguments(i2).size();
  }

  static int asBit(final boolean b) {
    return b ? 1 : 0;
  }

  /** Compare expressions by their number of characters
   * @param e1 JD
   * @param e2 JD
   * @return an integer which is either negative, zero, or positive, if the
   *         number of characters in the first argument is less than, equal to,
   *         or greater than the number of characters in the second argument. */
  static int characterCompare(final Expression e1, final Expression e2) {
    return countNonWhites(e1) - countNonWhites(e2);
  }

  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int countNonWhites(final ASTNode n) {
    return removeWhites(wizard.body(n)).length();
  }

  private static boolean isLonger(final Expression e1, final Expression e2) {
    return !hasNull(e1, e2) && (//
    nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD || //
        nodesCount(e1) >= nodesCount(e2) && moreArguments(e1, e2)//
    );
  }

  static class Int {
    int inner = 0;
  }

  /** Counts the number of statements in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int lineCount(final ASTNode n) {
    final Int $ = new Int();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode child) {
        if (Statement.class.isAssignableFrom(child.getClass()))
          f($, child);
      }

      void f(final Int $, final ASTNode ¢) {
        if (iz.is(¢, BLOCK)) {
          if (extract.statements(¢).size() > 1)
            ++$.inner;
          return;
        }
        if (iz.is(¢, EMPTY_STATEMENT))
          return;
        if (iz.is(¢, FOR_STATEMENT, ENHANCED_FOR_STATEMENT, DO_STATEMENT)) {
          $.inner += 4;
          return;
        }
        if (!iz.is(¢, IF_STATEMENT))
          $.inner += 3;
        else {
          $.inner += 4;
          if (step.elze(az.ifStatement(¢)) != null)
            ++$.inner;
        }
      }
    });
    return $.inner;
  }

  static int literalCompare(final Expression e1, final Expression e2) {
    return -specificity.compare(e1, e2);
  }

  /** Compare the length of the left and right arguments of an infix expression
   * @param x JD
   * @return <code><b>true</b></code> <i>iff</i> if the left operand of the
   *         parameter is is longer than the second argument */
  public static boolean longerFirst(final InfixExpression x) {
    return isLonger(step.left(x), step.right(x));
  }

  /** Compare method invocations by the number of arguments
   * @param e1 JD
   * @param e2 JD
   * @return <code><b>true</b></code> <i>iff</i> the first argument is a method
   *         invocation with more arguments that the second argument */
  public static boolean moreArguments(final Expression e1, final Expression e2) {
    return argumentsCompare(e1, e2) > 0;
  }

  static int nodesCompare(final Expression e1, final Expression e2) {
    return round(nodesCount(e1) - nodesCount(e2), NODES_THRESHOLD);
  }

  /** Counts the number of nodes in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int nodesCount(final ASTNode n) {
    class Integer {
      int inner = 0;
    }
    final Integer $ = new Integer();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(@SuppressWarnings("unused") final ASTNode __) {
        ++$.inner;
      }
    });
    return $.inner;
  }

  static int round(final int $, final int threshold) {
    return Math.abs($) > threshold ? $ : 0;
  }

  /** Sorts the {@link Expression} list
   * @param xs an {@link Expression} list to sort
   * @return True if the list was modified */
  public boolean sort(final List<Expression> xs) {
    boolean $ = false;
    // Bubble sort
    for (int i = 0, size = xs.size(); i < size; ++i)
      for (int j = 0; j < size - 1; ++j) {
        final Expression e0 = xs.get(j);
        final Expression e1 = xs.get(j + 1);
        if (iz.negative(e0) || iz.negative(e1) || compare(e0, e1) <= 0)
          continue;
        xs.remove(j);
        xs.remove(j);
        xs.add(j, e0);
        xs.add(j, e1);
        $ = true;
      }
    return $;
  }
}