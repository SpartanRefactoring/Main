package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.find.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class WhenHoldsOn<N extends ASTNode> extends NanoPatternTipper<N> {
  private static final long serialVersionUID = 0x756026178DF4792BL;

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "Go Fluent: Generalized Switch";
  }
  @Override public boolean canTip(final N ¢) {
    return !¢.equals(then(az.conditionalExpression(parent(¢))))//
        && differsInSingleAtomic(branchesExpressions(¢))//
        || differsInSingleExpression(branchesExpressions(¢));
  }
  List<Expression> branchesExpressions(final N ¢) {
    return branchesWrapper(¢).stream().map(step::expression).collect(toList());
  }
  @Override public Tip pattern(final N ¢) {
    return new Tip(description(¢), myClass(), ¢) {
      @Override @SuppressWarnings("unchecked") public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Expression> branchesExpressions = branchesExpressions(¢);
        r.replace(¢,
            make.ast("holds(" + notation.cent + " ->"
                + (differsInSingleAtomic(branchesExpressions(¢))
                    ? replaceAll(the.firstOf(branchesExpressions) + "", singleAtomicDifference(branchesExpressions), notation.cent) + ")"
                        + createOns(singleAtomicDifferences(branchesExpressions), (List<N>) branchesWrapper(¢)) + elseString(¢)
                    : replaceAll(the.firstOf(branchesExpressions) + "", singleExpressionDifference(branchesExpressions) + "", notation.cent) + ")"
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
  Collection<? extends ASTNode> branchesWrapper(final N ¢) {
    return !iz.conditionalExpression(¢) ? extract.branches(az.ifStatement(¢)) : extract.branches(az.conditionalExpression(¢));
  }
  /** [[SuppressWarningsSpartan]] */
  private String lastElseWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? extract.lastElse(az.ifStatement(¢)) : extract.lastElse(az.conditionalExpression(¢))) + "";
  }
  /** [[SuppressWarningsSpartan]] */
  private String thenWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? then(az.ifStatement(¢)) : then(az.conditionalExpression(¢))) + "";
  }
  static String replaceAll(final String target, final CharSequence oldString, final CharSequence newString) {
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
