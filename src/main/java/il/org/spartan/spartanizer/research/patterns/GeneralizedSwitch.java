package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
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
        && wizard.differsInSingleAtomic(branchesExpressions(¢));
  }

  static List<Expression> branchesExpressions(final IfStatement ¢) {
    return branches(¢).stream().map(x -> expression(x)).collect(Collectors.toList());
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      /** [[SuppressWarningsSpartan]] */
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        List<Expression> branchesExpressions = branchesExpressions(¢);
        String diff = wizard.findSingleAtomicDifference(branchesExpressions);
        r.replace(¢, wizard.ast("holds.on(" + "¢" + "->" + (first(branchesExpressions) + "").replaceAll(diff, "¢") + ");"), g);
      }
    };
  }
}
