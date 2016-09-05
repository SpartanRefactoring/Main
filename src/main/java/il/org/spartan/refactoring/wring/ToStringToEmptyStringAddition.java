package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.assemble.make.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @since 2016-8-31 */
public final class ToStringToEmptyStringAddition extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override String description(final MethodInvocation i) {
    final Expression receiver = step.receiver(i);
    return receiver == null ? "Use \"\" + x" : "Use \"\" + " + receiver;
  }

  @Override ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(step.name(i).getIdentifier()) || !i.arguments().isEmpty())
      return null;
    final Expression receiver = step.receiver(i);
    return receiver == null ? null
        : !(i.getParent() instanceof MethodInvocation) ? subject.pair(receiver, i.getAST().newStringLiteral()).to(InfixExpression.Operator.PLUS)
            : parethesized(subject.pair(receiver, i.getAST().newStringLiteral()).to(InfixExpression.Operator.PLUS));
  }
}
