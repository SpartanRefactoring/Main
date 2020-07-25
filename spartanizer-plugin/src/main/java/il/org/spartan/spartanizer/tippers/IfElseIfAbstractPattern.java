package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.safety.az;

/** An abstract pattern for <code>if() {} else if() {}</code>
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class IfElseIfAbstractPattern extends IfAbstractPattern {
  private static final long serialVersionUID = -0x4EE76127252F0ED7L;
  protected Expression elzeIfCondition;
  protected Statement elzeThen, elzeElze;

  public IfElseIfAbstractPattern() {
    andAlso("must have an else if", () -> {
      final IfStatement $ = az.ifStatement(elze);
      if ($ == null)
        return false;
      elzeIfCondition = $.getExpression();
      elzeThen = $.getThenStatement();
      elzeElze = $.getElseStatement();
      return true;
    });
  }
}
