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
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Sorting {
  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> $ = switchBranch.intoBranches(s);
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