package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>if (true) x; else {y;} </code> into <code>x;</code> and
 * <code>if (false) x; else {y;}  </code> into <code>
 * y;
 * </code> .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends ReplaceCurrentNode<IfStatement> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "if the condition is 'true'  convert to 'then' statement," + " if the condition is 'false' convert to 'else' statement";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && (literal.true¢(¢.getExpression()) || literal.false¢(¢.getExpression()));
  }
  @Override public Statement replacement(final IfStatement ¢) {
    return literal.true¢(¢.getExpression()) ? then(¢) : elze(¢) != null ? elze(¢) : ¢.getAST().newBlock();
  }
}
