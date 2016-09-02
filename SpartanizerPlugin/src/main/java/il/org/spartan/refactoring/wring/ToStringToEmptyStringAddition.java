package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import static il.org.spartan.refactoring.assemble.make.parethesized;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @since 2016-8-31 */
public final class ToStringToEmptyStringAddition extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(step.name(i).getIdentifier()) || !i.arguments().isEmpty())
      return null;
    final Expression receiver = step.receiver(i);
    return receiver == null ? null
        : i.getParent() instanceof MethodInvocation
            ? parethesized(subject.pair(i.getAST().newStringLiteral(), receiver).to(InfixExpression.Operator.PLUS))
            : subject.pair(i.getAST().newStringLiteral(), receiver).to(InfixExpression.Operator.PLUS);
  }

  @Override String description(final MethodInvocation i) {
    final Expression receiver = step.receiver(i);
    return receiver == null ? "Use \"\" + x" : "Use \"\" + " + receiver;
  }
}
