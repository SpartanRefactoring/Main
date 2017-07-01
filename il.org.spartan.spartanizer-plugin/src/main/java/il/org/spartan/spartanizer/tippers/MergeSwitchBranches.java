package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Removing redundant case branches in switch statement such as
 * {@code switch(x) { case a: x=1; break; case b: x=2; break; default: x=1;
 * break; } into {@code switch(x) { case a: default: x=1; break; case b: x=2;
 * break; } Tested in {@link Issue0858}
 * @author Yuval Simon
 * @since 2016-11-26 */
public class MergeSwitchBranches extends ReplaceCurrentNode<SwitchStatement>//
    implements Category.Collapse {
  private static final long serialVersionUID = 0x6463F526A06F20A9L;

  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<switchBranch> ret = switchBranch.intoBranches(s);
    if (ret.size() > switchBranch.MAX_CASES_FOR_SPARTANIZATION)
      return null;
    for (int i = 0; i < ret.size(); ++i)
      for (int j = i + 1; j < ret.size(); ++j)
        if (ret.get(i).hasSameBody(ret.get(j))) {
          ret.get(i).cases.addAll(ret.get(j).cases);
          ret.remove(j);
          return switchBranch.makeSwitchStatement(ret, step.expression(s), s.getAST());
        }
    return null;
  }
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Merge branches with same code";
  }
}
