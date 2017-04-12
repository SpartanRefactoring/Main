package il.org.spartan.research.nanos;

import static il.org.spartan.research.nanos.common.NanoPatternUtil.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** Like {@link NotNullAssumed} but in the beginning of a method.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public final class PreconditionNotNull extends NotNullAssumed {
  private static final long serialVersionUID = -0x11AEDC7B1565B34AL;

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && first(statements(az.methodDeclaration(parent(parent(¢))))) == ¢;
  }
}
