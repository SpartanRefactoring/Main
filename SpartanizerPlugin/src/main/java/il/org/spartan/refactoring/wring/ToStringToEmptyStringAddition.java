package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;


import il.org.spartan.refactoring.utils.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @since 2016-8-31 */
public final class ToStringToEmptyStringAddition extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override ASTNode replacement(MethodInvocation n) {
    if(!"toString".equals(step.name(n).getIdentifier()))
      return null;
    final Expression receiver = step.receiver(n);
    return receiver == null ? null : subject.pair((n.getAST()).newStringLiteral(),receiver).to(InfixExpression.Operator.PLUS);
      
    }
  @Override String description(final MethodInvocation n) {
    final Expression receiver = step.receiver(n);
    return receiver == null ? "Use \"\" + x" : "Use \"\" + " + receiver;
  }
  
}
