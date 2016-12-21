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
public final class GeneralizedSwitch extends NanoPatternTipper<IfStatement> {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Go Fluent: Generalized Switch";
  }

  @Override public boolean canTip(final IfStatement ¢) {
    return !¢.equals(then(az.ifStatement(parent(¢))))//
        && differsInSingleAtomic(branchesExpressions(¢));
  }

  static List<Expression> branchesExpressions(final IfStatement ¢) {
    return branches(¢).stream().map(x -> expression(x)).collect(Collectors.toList());
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final List<Expression> branchesExpressions = branchesExpressions(¢);
        r.replace(¢, ast("holds(λ ->" + (first(branchesExpressions) + "").replaceAll(findSingleAtomicDifference(branchesExpressions), "λ") + ")"
            + createOns(findSingleAtomicDifferences(branchesExpressions), branches(¢)) + elseSring(¢) + ";"), g);
      }
    };
  }

  static String elseSring(final IfStatement ¢) {
    return lastElse(¢) == null ? "" : ".elze(() -> {" + lastElse(¢) + "})";
  }

  static String createOns(final List<String> diffs, final List<IfStatement> branches) {
    assert diffs.size() == branches.size();
    String $ = "";
    for (int ¢ = 0; ¢ < diffs.size(); ++¢)
      $ += ".on(" + diffs.get(¢) + ",() -> {" + then(branches.get(¢)) + "})";
    return $;
  }
}
