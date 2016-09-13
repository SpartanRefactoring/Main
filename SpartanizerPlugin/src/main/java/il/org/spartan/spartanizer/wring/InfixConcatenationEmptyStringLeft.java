package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Convert <code>""+x</code> to <code>x+""</code>
 * @author Dan Greenstein
 * @since 2016 */
public class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression> implements Kind.Collapse {
  private static InfixExpression replace(final InfixExpression x) {
    return subject.pair(duplicate.of(right(x)), duplicate.of(left(x))).to(wizard.PLUS2);
  }

  @Override public String description(final InfixExpression x) {
    return "Append, rather than prepend, \"\", to " + left(x);
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    return !iz.emptyStringLiteral(left(x)) || !iz.infixPlus(x) ? null : replace(x);
  }
}
