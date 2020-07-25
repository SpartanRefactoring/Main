package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.research.nanos.common.NanoPatternUtil.nullCheck;

import org.eclipse.jdt.core.dom.IfStatement;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.safety.az;

/** Like {@link NotNullAssumed} but in the beginning of a method.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-08 */
public final class NonNullRequired extends NotNullAssumed {
  private static final long serialVersionUID = -0x11AEDC7B1565B34AL;

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(expression(¢))//
        && the.firstOf(statements(az.methodDeclaration(parent(parent(¢))))) == ¢;
  }
}
