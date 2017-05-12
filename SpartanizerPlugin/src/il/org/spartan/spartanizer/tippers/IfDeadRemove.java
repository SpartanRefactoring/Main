package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify if statements as much as possible (or remove them or parts of them)
 * if and only if it doesn't have any side-effect.
 * @author Dor Ma'ayan
 * @since 2016-09-26 */
public class IfDeadRemove extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.EmptyCycles {
  private static final long serialVersionUID = -0x42405A1924425155L;

  @Override public String description(final IfStatement ¢) {
    return "Remove empty cycles in: " + ¢;
  }

  @Override public ASTNode replacement(final IfStatement ¢) {
    return !sideEffects.free(¢) ? null : ¢.getAST().newBlock();
  }
}
