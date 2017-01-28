package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.lisp.first;
import static il.org.spartan.spartanizer.ast.navigate.find.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @year 2016
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class GeneralizedSwitch<N extends ASTNode> extends NanoPatternTipper<N> {
  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "Go Fluent: Generalized Switch";
  }

  @Override public boolean canTip(final N ¢) {
    return !¢.equals(then(az.conditionalExpression(parent(¢))))//
        && differsInSingleAtomic(branchesExpressions(¢))//
        || differsInSingleExpression(branchesExpressions(¢));
  }

  List<Expression> branchesExpressions(final N ¢) {
    return branchesWrapper(¢).stream().map(step::expression).collect(Collectors.toList());
  }

  @Override public Tip pattern(final N ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override @SuppressWarnings("unchecked") public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Expression> branchesExpressions = branchesExpressions(¢);
        r.replace(¢,
            ast("holds(" + namer.current + " ->"
                + (differsInSingleAtomic(branchesExpressions(¢))
                    ? replaceAll(first(branchesExpressions) + "", singleAtomicDifference(branchesExpressions), namer.current) + ")"
                        + createOns(singleAtomicDifferences(branchesExpressions), (List<N>) branchesWrapper(¢)) + elseString(¢)
                    : replaceAll(first(branchesExpressions) + "", singleExpressionDifference(branchesExpressions) + "", namer.current) + ")"
                        + createExpressionOns(findSingleExpressionDifferences(branchesExpressions), (List<N>) branchesWrapper(¢)) + elseString(¢))),
            g);
      }
    };
  }

  String createExpressionOns(final List<Expression> diffs, final List<N> branches) {
    assert diffs.size() == branches.size();
    String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(() ->" + diffs.get(¢) + ",() -> " + extractSemicolonIfNeeded(thenWrapper(branches.get(¢))) + ")";
    return $;
  }

  private static String extractSemicolonIfNeeded(final String ¢) {
    final String $ = ¢.replaceAll("\n", "");
    return $ == null || !$.endsWith(";") ? $ : $.substring(0, $.length() - 1);
  }

  String elseString(final N ¢) {
    return lastElseWrapper(¢) == null ? "" : ".elze(() -> " + extractSemicolonIfNeeded(lastElseWrapper(¢)) + ")" + (iz.ifStatement(¢) ? ";" : "");
  }

  String createOns(final List<String> diffs, final List<? extends N> branches) {
    assert diffs.size() == branches.size();
    String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(" + diffs.get(¢) + ",() -> " + extractSemicolonIfNeeded(thenWrapper(branches.get(¢))) + ")";
    return $;
  }

  /** [[SuppressWarningsSpartan]] */
  List<? extends ASTNode> branchesWrapper(final N ¢) {
    return !iz.conditionalExpression(¢) ? branches(az.ifStatement(¢)) : branches(az.conditionalExpression(¢));
  }

  /** [[SuppressWarningsSpartan]] */
  private String lastElseWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? lastElse(az.ifStatement(¢)) : lastElse(az.conditionalExpression(¢))) + "";
  }

  /** [[SuppressWarningsSpartan]] */
  private String thenWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? then(az.ifStatement(¢)) : then(az.conditionalExpression(¢))) + "";
  }

  static String replaceAll(final String target, final String oldString, final String newString) {
    String $ = target;
    while (!$.replace(oldString, newString).equals($))
      $ = $.replace(oldString, newString);
    return $;
  }

  @Override public Category category() {
    return Category.Conditional;
  }

  @Override public String description() {
    return "A generalized form of a switch where the switchee can be a Lambda function";
  }

  @Override public String technicalName() {
    return "if(...) $N1($X); else if(...) $N2($X); ... else $Nk($X);";
  }
}
