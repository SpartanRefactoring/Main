package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** Like {@link NotNullOrReturn} but in the beginning of a method.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
public final class PreconditionNotNull extends NotNullOrReturn {
  private static final long serialVersionUID = -1274198165790503754L;

  @Override public boolean check(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && first(statements(az.methodDeclaration(parent(parent(¢))))) == ¢;
  }
}
