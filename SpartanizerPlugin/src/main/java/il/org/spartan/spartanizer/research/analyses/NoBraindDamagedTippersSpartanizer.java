package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;

/** A Spartanizer without heavy or buggy tippers
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public class NoBraindDamagedTippersSpartanizer extends InteractiveSpartanizer {
  public NoBraindDamagedTippersSpartanizer() {
    removeHeavy();
  }

  private NoBraindDamagedTippersSpartanizer removeHeavy() {
    remove(SwitchStatement.class, //
        new MergeSwitchBranches(), //
        new RemoveRedundantSwitchReturn(), //
        new RemoveRedundantSwitchContinue(), //
        new SwitchWithOneCaseToIf(), //
        new SwitchBranchSort(), //
        null)//
            .remove(SwitchCase.class, //
                new RemoveRedundantSwitchCases(), //
                new SwitchCaseLocalSort(), //
                null);
    return this;
  }
}
