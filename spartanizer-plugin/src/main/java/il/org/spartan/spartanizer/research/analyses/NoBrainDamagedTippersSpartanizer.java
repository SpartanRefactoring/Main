package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;

import il.org.spartan.spartanizer.cmdline.good.InteractiveSpartanizer;
import il.org.spartan.spartanizer.tippers.MergeSwitchBranches;
import il.org.spartan.spartanizer.tippers.RemoveRedundantSwitchCases;
import il.org.spartan.spartanizer.tippers.RemoveRedundantSwitchContinue;
import il.org.spartan.spartanizer.tippers.RemoveRedundantSwitchReturn;
import il.org.spartan.spartanizer.tippers.SwitchBranchSort;
import il.org.spartan.spartanizer.tippers.SwitchCaseLocalSort;
import il.org.spartan.spartanizer.tippers.SwitchSingleCaseToIf;

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
