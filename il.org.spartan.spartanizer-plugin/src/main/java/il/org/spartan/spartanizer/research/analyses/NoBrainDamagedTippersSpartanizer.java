package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;

/** A spartanizer without heavy or buggy tippers
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public class NoBrainDamagedTippersSpartanizer extends InteractiveSpartanizer {
  public NoBrainDamagedTippersSpartanizer() {
    removeHeavy();
  }
  private NoBrainDamagedTippersSpartanizer removeHeavy() {
    remove(SwitchStatement.class, //
        new MergeSwitchBranches(), //
        new RemoveRedundantSwitchReturn(), //
        new RemoveRedundantSwitchContinue(), //
        new SwitchSingleCaseToIf(), //
        new SwitchBranchSort(), //
        null)//
            .remove(SwitchCase.class, //
                new RemoveRedundantSwitchCases(), //
                new SwitchCaseLocalSort(), //
                null);
    return this;
  }
}
