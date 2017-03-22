package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.find.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.first;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public final class GeneralizedSwitch<N extends ASTNode> extends NanoPatternTipper<N> {
  private static final long serialVersionUID = 8457801982809504043L;

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "Go Fluent: Generalized Switch";
  }

  @Override public boolean canTip(@NotNull final N ¢) {
    return !¢.equals(then(az.conditionalExpression(parent(¢))))//
        && differsInSingleAtomic(branchesExpressions(¢))//
        || differsInSingleExpression(branchesExpressions(¢));
  }

  List<Expression> branchesExpressions(final N ¢) {
    return branchesWrapper(¢).stream().map(step::expression).collect(toList());
  }

  @NotNull
  @Override public Tip pattern(@NotNull final N ¢) {
    return new Tip(description(¢), ¢, myClass()) {
      @Override @SuppressWarnings("unchecked") public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        final List<Expression> branchesExpressions = branchesExpressions(¢);
        r.replace(¢,
            ast("holds(" + namer.it + " ->"
                + (differsInSingleAtomic(branchesExpressions(¢))
                    ? replaceAll(first(branchesExpressions) + "", singleAtomicDifference(branchesExpressions), namer.it) + ")"
                        + createOns(singleAtomicDifferences(branchesExpressions), (List<N>) branchesWrapper(¢)) + elseString(¢)
                    : replaceAll(first(branchesExpressions) + "", singleExpressionDifference(branchesExpressions) + "", namer.it) + ")"
                        + createExpressionOns(findSingleExpressionDifferences(branchesExpressions), (List<N>) branchesWrapper(¢)) + elseString(¢))),
            g);
      }
    };
  }

  @NotNull String createExpressionOns(@NotNull final List<Expression> diffs, @NotNull final List<N> branches) {
    assert diffs.size() == branches.size();
    @NotNull String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(() ->" + diffs.get(¢) + ",() -> " + extractSemicolonIfNeeded(thenWrapper(branches.get(¢))) + ")";
    return $;
  }

  @NotNull
  private static String extractSemicolonIfNeeded(@NotNull final String ¢) {
    final String $ = ¢.replaceAll("\n", "");
    return $ == null || !$.endsWith(";") ? $ : $.substring(0, $.length() - 1);
  }

  @NotNull String elseString(final N ¢) {
    return lastElseWrapper(¢) == null ? "" : ".elze(() -> " + extractSemicolonIfNeeded(lastElseWrapper(¢)) + ")" + (iz.ifStatement(¢) ? ";" : "");
  }

  @NotNull String createOns(@NotNull final List<String> diffs, @NotNull final List<? extends N> branches) {
    assert diffs.size() == branches.size();
    @NotNull String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(" + diffs.get(¢) + ",() -> " + extractSemicolonIfNeeded(thenWrapper(branches.get(¢))) + ")";
    return $;
  }

  /** [[SuppressWarningsSpartan]] */
  @Nullable Collection<? extends ASTNode> branchesWrapper(final N ¢) {
    return !iz.conditionalExpression(¢) ? extract.branches(az.ifStatement(¢)) : extract.branches(az.conditionalExpression(¢));
  }

  /** [[SuppressWarningsSpartan]] */
  @NotNull
  private String lastElseWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? extract.lastElse(az.ifStatement(¢)) : extract.lastElse(az.conditionalExpression(¢))) + "";
  }

  /** [[SuppressWarningsSpartan]] */
  @NotNull
  private String thenWrapper(final N ¢) {
    return (!iz.conditionalExpression(¢) ? then(az.ifStatement(¢)) : then(az.conditionalExpression(¢))) + "";
  }

  static String replaceAll(final String target, @NotNull final CharSequence oldString, @NotNull final CharSequence newString) {
    String $ = target;
    while (!$.replace(oldString, newString).equals($))
      $ = $.replace(oldString, newString);
    return $;
  }

  @Override public String nanoName() {
    return "WhenHoldsOn";
  }
}
