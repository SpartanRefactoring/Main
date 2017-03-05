package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch Test case is {@link Issue0861}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-11 */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -3538528399312058847L;

  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> $ = switchBranch.intoBranches(s);
    if ($.size() > switchBranch.MAX_CASES_FOR_SPARTANIZATION)
      return null;
    for (int ¢ = 0; ¢ < $.size() - 1; ++¢) {
      final switchBranch current = $.get(¢), next = $.get(¢ + 1);
      if (current.gt(next)) {
        final switchBranch tmp = next;
        $.set(¢ + 1, current);
        $.set(¢, tmp);
        return switchBranch.makeSwitchStatement($, expression(s), s.getAST());
      }
    }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Sort switch branches";
  }
}