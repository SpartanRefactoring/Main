package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Removing redundant case branches in switch statement such as
 * {@code switch(x) { case a: x=1; break; case b: x=2; break; default: x=1;
 * break; } into {@code switch(x) { case a: default: x=1; break; case b: x=2;
 * break; } Tested in {@link Issue0858}
 * @author Yuval Simon
 * @since 2016-11-26 */
public class MergeSwitchBranches extends ReplaceCurrentNode<SwitchStatement>//
    implements TipperCategory.Unite {
  @Nullable
  @Override public ASTNode replacement(@NotNull final SwitchStatement s) {
    final List<switchBranch> $ = switchBranch.intoBranches(s);
    if ($.size() > switchBranch.MAX_CASES_FOR_SPARTANIZATION)
      return null;
    for (int i = 0; i < $.size(); ++i)
      for (int j = i + 1; j < $.size(); ++j)
        if ($.get(i).hasSameBody($.get(j))) {
          $.get(i).cases().addAll($.get(j).cases());
          $.remove(j);
          return switchBranch.makeSwitchStatement($, step.expression(s), s.getAST());
        }
    return null;
  }

  @NotNull
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merge branches with same code";
  }
}
