package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class GeneralizedSwitchTernary extends NanoPatternTipper<ConditionalExpression> {
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Go Fluent: Generalized Switch";
  }

  @Override public boolean canTip(final ConditionalExpression ¢) {
    return !¢.equals(then(az.conditionalExpression(parent(¢))))//
        && differsInSingleAtomic(branchesExpressions(¢)) || differsInSingleExpression(branchesExpressions(¢));
  }

  static List<Expression> branchesExpressions(final ConditionalExpression ¢) {
    return branches(¢).stream().map(x -> expression(x)).collect(Collectors.toList());
  }

  @Override public Tip pattern(final ConditionalExpression ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Expression> branchesExpressions = branchesExpressions(¢);
        if (differsInSingleAtomic(branchesExpressions(¢)))
          r.replace(¢, ast("holds(λ ->" + (first(branchesExpressions) + "").replaceAll(findSingleAtomicDifference(branchesExpressions), "λ") + ")"
              + createOns(findSingleAtomicDifferences(branchesExpressions), branches(¢)) + elseSring(¢)), g);
        else
          r.replace(¢, ast("holds(λ ->" + replaceAll(first(branchesExpressions) + "", "" + findSingleExpressionDifference(branchesExpressions), "λ")
              + ")" + createExpressionOns(findSingleExpressionDifferences(branchesExpressions), branches(¢)) + elseSring(¢)), g);
      }
    };
  }

  static String createExpressionOns(final List<Expression> diffs, final List<ConditionalExpression> branches) {
    assert diffs.size() == branches.size();
    String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(__ ->" + diffs.get(¢) + ",__ -> " + then(branches.get(¢)) + ")";
    return $;
  }

  static String elseSring(final ConditionalExpression ¢) {
    return lastElse(¢) == null ? "" : ".elze(__ -> " + lastElse(¢) + ")";
  }

  static String createOns(final List<String> diffs, final List<ConditionalExpression> branches) {
    assert diffs.size() == branches.size();
    String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(" + diffs.get(¢) + ",__ -> " + then(branches.get(¢)) + ")";
    return $;
  }

  static String replaceAll(final String target, final String oldString, final String newString) {
    String $ = target;
    while (!$.replace(oldString, newString).equals($))
      $ = $.replace(oldString, newString);
    return $;
  }
}
