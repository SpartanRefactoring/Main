package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.assemble.make.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Collapse {
  @Override String description(final MethodInvocation i) {
    final Expression receiver = step.receiver(i);
    return "Use \"\" + " + (receiver == null ? "x" : receiver + "");
  }

  @Override ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(step.name(i).getIdentifier()) || !i.arguments().isEmpty())
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
