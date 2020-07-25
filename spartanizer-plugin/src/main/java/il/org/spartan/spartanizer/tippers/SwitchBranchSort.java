package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.spartanizer.ast.navigate.switchBranch;
import il.org.spartan.spartanizer.issues.Issue0861;
import il.org.spartan.spartanizer.issues.Issue1147;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch Test case is {@link Issue0861} and
 * {@link Issue1147}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-11 */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0x311B60D4CCA079DFL;

  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> $ = switchBranch.intoBranches(s);
    if ($.size() > switchBranch.MAX_CASES_FOR_SPARTANIZATION)
      return null;
    for (int ¢ = 0; ¢ < $.size() - 1; ++¢)
      if (!$.get(¢).compareTo($.get(¢ + 1))) {
        final switchBranch tmp = $.get(¢ + 1);
        $.set(¢ + 1, $.get(¢));
        $.set(¢, tmp);
        return switchBranch.makeSwitchStatement($, expression(s), s.getAST());
      }
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Sort switch branches";
  }
}