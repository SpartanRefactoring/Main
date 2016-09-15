package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.assemble.make.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends ReplaceCurrentNode<MethodInvocation> implements Kind.Collapse {
  @Override public String description(final MethodInvocation i) {
    final Expression receiver = step.receiver(i);
    return "Use \"\" + " + (receiver == null ? "x" : receiver + "");
  }

  @Override public ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(step.name(i).getIdentifier()) || !i.arguments().isEmpty() || iz.expressionStatement(i.getParent()))
      return null;
    final Expression receiver = step.receiver(i);
    return receiver == null ? null : !(i.getParent() instanceof MethodInvocation) ? subject.pair(receiver, //
        il.org.spartan.spartanizer.assemble.make.makeStringLiteral(i)//
    ).to(InfixExpression.Operator.PLUS)
        : parethesized(subject.pair(//
            receiver, //
            il.org.spartan.spartanizer.assemble.make.makeStringLiteral(i)).to(InfixExpression.Operator.PLUS));
  }
}
