package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import java.util.List;

import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.switchBranch;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.issues.Issue0860;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** sorts cases of a local branch {@code switch(x) { case 2: case 1: break; }}
 * to {@code switch(x) { case 1: case 2: break; } } Tests are in
 * {@link Issue0860}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-09 */
public class SwitchCaseLocalSort extends CarefulTipper<SwitchCase>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = 0x3FBC0D3028B5DF0L;

  @Override public Tip tip(final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInBlock(n));
    return new Tip(description(n), getClass(), n) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(n, copy.of($), g);
        r.replace($, copy.of(n), g);
      }
    }.spanning($);
  }
  @Override protected boolean prerequisite(final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInBlock(n));
    final List<SwitchCase> cases = extract.casesOnSameBranch(az.switchStatement(parent(n)), n);
    return cases.size() <= switchBranch.MAX_CASES_FOR_SPARTANIZATION && cases.stream().noneMatch(SwitchCase::isDefault) && $ != null && !$.isDefault()
        && !n.isDefault() && (iz.intType(expression(n)) || (expression(n) + "").compareTo(expression($) + "") > 0)
        && (!iz.intType(expression(n)) || Integer.parseInt(expression(n) + "") > Integer.parseInt(expression($) + ""));
  }
  @Override @SuppressWarnings("unused") public String description(final SwitchCase n) {
    return "sort cases with same flow control";
  }
}