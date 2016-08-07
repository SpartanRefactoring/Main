package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;

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
  private static Specificity specificity = new Specificity();

  static int literalCompare(final Expression e1, final Expression e2) {
    return -specificity.compare(e1, e2);
  }
  static int nodesCompare(final Expression e1, final Expression e2) {
    return round(nodesCount(e1) - nodesCount(e2), NODES_THRESHOLD);
  }
  static int argumentsCompare(final Expression e1, final Expression e2) {
    return !Is.methodInvocation(e1) || !Is.methodInvocation(e2) ? 0 : argumentsCompare((MethodInvocation) e1, (MethodInvocation) e2);
  }
  static int argumentsCompare(final MethodInvocation i1, final MethodInvocation i2) {
    return i1.arguments().size() - i2.arguments().size();
  }
  /** Compare method invocations by the number of arguments
   * @param e1 JD
   * @param e2 JD
   * @return <code><b>true</b></code> <i>iff</i> the first argument is a method
   *         invocation with more arguments that the second argument */
  public static boolean moreArguments(final Expression e1, final Expression e2) {
    return argumentsCompare(e1, e2) > 0;
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
  /** Lexicographical comparison expressions by their number of characters
   * @param e1 JD
   * @param e2 JD
   * @return an integer which is either negative, zero, or positive, if the
   *         number of characters in the first argument occurs before, at the
   *         same place, or after then the second argument in lexicographical
   *         order. */
  static int alphabeticalCompare(final Expression e1, final Expression e2) {
    return asString(e1).compareTo(asString(e2));
  }
  static int round(final int $, final int threshold) {
    return Math.abs($) > threshold ? $ : 0;
  }
  static int asBit(final boolean b) {
    return b ? 1 : 0;
  }
  /** Compare the length of the left and right arguments of an infix expression
   * @param e JD
   * @return <code><b>true</b></code> <i>iff</i> if the left operand of the
   *         parameter is is longer than the second argument */
  public static boolean longerFirst(final InfixExpression e) {
    return isLonger(left(e), right(e));
  }
  private static boolean isLonger(final Expression e1, final Expression e2) {
    return !hasNulls(e1, e2) && (//
    nodesCount(e1) > nodesCount(e2) + NODES_THRESHOLD || //
        nodesCount(e1) >= nodesCount(e2) && moreArguments(e1, e2)//
    );
  }

  /** Threshold for comparing nodes; a difference in the number of nodes between
   * two nodes is considered zero, if it is the less than this value, */
  public static final int NODES_THRESHOLD = 1;

  /** Counts the number of non-space characters in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int countNonWhites(final ASTNode n) {
    return asString(n).length();
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
  /** Counts the number of statements in a tree rooted at a given node
   * @param n JD
   * @return Number of abstract syntax tree nodes under the parameter. */
  public static int lineCount(final ASTNode n) {
    class Integer {
      int inner = 0;
    }
    final Integer $ = new Integer();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode child) {
        if (Statement.class.isAssignableFrom(child.getClass()))
          switch (child.getNodeType()) {
            case BLOCK:
              if (extract.statements(child).size() > 1)
                ++$.inner;
              return;
            case EMPTY_STATEMENT:
              return;
            case FOR_STATEMENT:
            case ENHANCED_FOR_STATEMENT:
            case DO_STATEMENT:
              $.inner += 4;
              return;
            case IF_STATEMENT:
              $.inner += 4;
              final IfStatement i = asIfStatement(child);
              if (elze(i) != null)
                ++$.inner;
              return;
            default:
              $.inner += 3;
          }
      }
    });
    return $.inner;
  }
  /** Sorts the {@link Expression} list
   * @param es an {@link Expression} list to sort
   * @return True if the list was modified */
  public boolean sort(final List<Expression> es) {
    boolean $ = false;
    // Bubble sort
    for (int i = 0, size = es.size(); i < size; ++i)
      for (int j = 0; j < size - 1; ++j) {
        final Expression e0 = es.get(j);
        final Expression e1 = es.get(j + 1);
        if (Is.negative(e0) || Is.negative(e1) || compare(e0, e1) <= 0)
          continue;
        es.remove(j);
        es.remove(j);
        es.add(j, e0);
        es.add(j, e1);
        $ = true;
      }
    return $;
  }
}