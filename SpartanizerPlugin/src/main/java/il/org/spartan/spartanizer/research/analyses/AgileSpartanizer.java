package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tippers.*;

/** A Spartanizer without heavy or buggy tippers
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public class AgileSpartanizer extends InteractiveSpartanizer {
  public AgileSpartanizer() {
    removeHeavy();
  }

  private AgileSpartanizer removeHeavy() {
    remove(SwitchStatement.class, //
        new SwitchEmpty(), //
        new MergeSwitchBranches(), //
        new RemoveRedundantSwitchReturn(), //
        new RemoveRedundantSwitchContinue(), //
        new SwitchWithOneCaseToIf(), //
        new SwitchBranchSort(), //
        null)//
            .remove(InfixExpression.class, //
                new InfixAdditionSubtractionExpand(), //
                null) //
            .remove(SwitchCase.class, //
                new RemoveRedundantSwitchCases(), //
                new SwitchCaseLocalSort(), //
                null);
    return this;
  }
}
