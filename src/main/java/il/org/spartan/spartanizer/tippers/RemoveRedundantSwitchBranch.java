package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removing redundant case branches in switch statement such as
 *
 * <pre>
* switch(x) {
* case a: x=1; break;
* case b: x=2; break;
* default: x=1; break;
 * </pre>
 *
 * into
 *
 * <pre>
* switch(x) {
* case a: default: x=1; break;
* case b: x=2; break;
 * </pre>
 *
 * Tested in {@link Issue0858}
 * @author Yuval Simon
 * @since 2016-11-26 */
public class RemoveRedundantSwitchBranch extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> $ = switchBranch.intoBranches(s);
    for (int i = 0; i < $.size(); ++i)
      for (int j = i + 1; j < $.size(); ++j)
        if ($.get(i).hasSameCode($.get(j))) {
          $.get(i).cases().addAll($.get(j).cases());
          $.remove(j);
          return switchBranch.makeSwitchStatement($, step.expression(s), s.getAST());
        }
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merge branches with same code";
  }
}
