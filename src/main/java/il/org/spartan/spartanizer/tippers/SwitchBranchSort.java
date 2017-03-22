package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch Test case is {@link Issue0861}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-11 */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -3538528399312058847L;

  @Override @Nullable public ASTNode replacement(@NotNull final SwitchStatement s) {
    @NotNull final List<switchBranch> $ = switchBranch.intoBranches(s);
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

  @Override @NotNull public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Sort switch branches";
  }
}