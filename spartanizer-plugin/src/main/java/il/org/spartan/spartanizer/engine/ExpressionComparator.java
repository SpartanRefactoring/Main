package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;

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
      return ($ = literalCompare(e1, e2)) == 0 && ($ = nodesCompare(e1, e2)) == 0 && ($ = characterCompare(e1, e2)) == 0
          && ($ = alphabeticalCompare(e1, e2)) == 0 ? 0 : $;
    }
  },
  /** Order on terms in addition: except that we do not sort alphabetically
   * @author Yossi Gil
   * @since 2015-07-19 */
  PRUDENT {
    @Override public int compare(final Expression e1, final Expression e2) {
      int $;
      return ($ = literalCompare(e1, e2)) == 0 && ($ = nodesCompare(e1, e2)) == 0 && ($ = characterCompare(e1, e2)) == 0 ? 0 : $;
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
  private static final Comparator<Expression> specificity = new specificity();
  /** Threshold for comparing nodes; a difference in the number of nodes between
   * two nodes is considered zero, if it is the less than this value, */
  public static final int NODES_THRESHOLD = 1;

  /** Compare the length of the left and right arguments of an infix expression
   * @param x JD
   * @return whether if the left operand of the parameter is is longer than the
   *         second argument */
  public static boolean longerFirst(final InfixExpression ¢) {
    return isLonger(left(¢), right(¢));
  }
  /** Compare method invocations by the number of arguments
   * @param e1 JD
   * @param e2 JD
   * @return whether the first argument is a method invocation with more
   *         arguments that the second argument */
  public static boolean moreArguments(final Expression e1, final Expression e2) {
    return argumentsCompare(e1, e2) > 0;
  }
  /** Lexicographical comparison expressions by their number of characters
   * @param e1 JD
   * @param e2 JD
   * @return an integer which is either negative, zero, or positive, if the
   *         number of characters in the first argument occurs before, at the
   *         same place, or after then the second argument in lexicographical
   *         order. */
  static int alphabeticalCompare(final Expression e1, final Expression e2) {
    return removeWhites(Trivia.cleanForm(e1)).compareTo(removeWhites(Trivia.cleanForm(e2)));
  }
  static int argumentsCompare(final Expression e1, final Expression e2) {
    return !iz.methodInvocation(e1) || !iz.methodInvocation(e2) ? 0 : argumentsCompare((MethodInvocation) e1, (MethodInvocation) e2);
  }
  static int argumentsCompare(final MethodInvocation i1, final MethodInvocation i2) {
    return arguments(i1).size() - arguments(i2).size();
  }
  /** Compare expressions by their number of characters
   * @param e1 JD
   * @param e2 JD
   * @return an integer which is either negative, zero, or positive, if the
   *         number of characters in the first argument is less than, equal to,
   *         or greater than the number of characters in the second argument. */
  static int characterCompare(final Expression e1, final Expression e2) {
    return countOf.nonWhiteCharacters(e1) - countOf.nonWhiteCharacters(e2);
  }
  static int literalCompare(final Expression e1, final Expression e2) {
    return -specificity.compare(e1, e2);
  }
  static int nodesCompare(final Expression e1, final Expression e2) {
    return round(countOf.nodes(e1) - countOf.nodes(e2), NODES_THRESHOLD);
  }
  static int round(final int $, final int threshold) {
    return Math.abs($) > threshold ? $ : 0;
  }
  private static boolean isLonger(final Expression e1, final Expression e2) {
    return !has.nil(e1, e2) && (//
    countOf.nodes(e1) > countOf.nodes(e2) + NODES_THRESHOLD || //
        countOf.nodes(e1) >= countOf.nodes(e2) && moreArguments(e1, e2)//
    );
  }
  /** Sorts the {@link Expression} list
   * @param xs an {@link Expression} list to sort
   * @return whether the list was modified */
  public boolean sort(final List<Expression> xs) {
    boolean $ = false;
    // Bubble sort
    for (int i = 0, size = xs.size(); i < size; ++i)
      for (int j = 0; j < size - 1; ++j) {
        final Expression e0 = xs.get(j), e1 = xs.get(j + 1);
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