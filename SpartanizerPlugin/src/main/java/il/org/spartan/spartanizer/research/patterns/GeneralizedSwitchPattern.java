package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import il.org.spartan.spartanizer.utils.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public final class GeneralizedSwitchPattern extends NanoPatternTipper<IfStatement> {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "replace with precondition.notNull($X)";
  }

  /** [[SuppressWarningsSpartan]] */
  @Override public boolean canTip(final IfStatement ¢) {
    final List<IfStatement> $ = branches(¢);
    if ($ == null || $.isEmpty())
      return false;
    final Expression e = expression(first($));
    final Str divergent = new Str("");
    return $.stream().map(s -> expression(s)).allMatch(s -> wizard.same(e, s) || differsByTheOneIdentifier(e, s, divergent));
  }

  private static boolean differsByTheOneIdentifier(Expression e1, Expression e2, Str divergent) {
    final String $ = wizard.findSingleNameDifference(e1, e2);
    return $ != null && ("".equals($) && (divergent.inner = $) != null || $.equals(divergent.inner));
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(¢, wizard.ast("GeneralizedSwithch();"), g);
      }
    };
  }
}
