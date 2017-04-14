package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch Test case is {@link Issue0861} and
 * {@link Issue1147}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2017-01-11 */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Sorting {
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