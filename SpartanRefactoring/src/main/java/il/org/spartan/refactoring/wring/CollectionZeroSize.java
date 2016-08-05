package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

/** A {@link Wring} to change emptiness check from
 * <code><pre>x.size() != 0</pre></code> or <code><pre>x.size() > 0</pre></code>
 * to <code><pre>!x.isEmpty()</pre></code>. TODO add tests
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24 */
public class CollectionZeroSize extends Wring.ReplaceCurrentNode<InfixExpression> {
  // list of accepted operators
  final List<InfixExpression.Operator> ao = Arrays
      .asList(new Operator[] { InfixExpression.Operator.EQUALS, InfixExpression.Operator.NOT_EQUALS, InfixExpression.Operator.GREATER });

  @Override ASTNode replacement(final InfixExpression e) {
    if (!e.getAST().hasResolvedBindings() || !(e.getLeftOperand() instanceof MethodInvocation) || !(e.getRightOperand() instanceof NumberLiteral)
        || !ao.contains(e.getOperator()))
      return null;
    final MethodInvocation mi = (MethodInvocation) e.getLeftOperand();
    final NumberLiteral nl = (NumberLiteral) e.getRightOperand();
    if (!"size".equals(mi.getName().getIdentifier()) || Double.parseDouble(nl.getToken()) != 0)
      return null;
    final Expression mie = mi.getExpression();
    final IMethodBinding iemb = BindingUtils.getVisibleMethod(mie != null ? mie.resolveTypeBinding() : BindingUtils.getClass(e), "isEmpty", null, e,
        compilationUnit);
    if (iemb == null || !"boolean".equals(iemb.getReturnType().toString()) && !"java.lang.Boolean".equals(iemb.getReturnType().getBinaryName()))
      return null;
    final MethodInvocation ie = e.getAST().newMethodInvocation();
    ie.setExpression((Expression) ASTNode.copySubtree(e.getAST(), mi.getExpression()));
    ie.setName(e.getAST().newSimpleName("isEmpty"));
    ASTNode $;
    if (e.getOperator().equals(InfixExpression.Operator.EQUALS))
      $ = ie;
    else {
      $ = e.getAST().newPrefixExpression();
      ((PrefixExpression) $).setOperator(PrefixExpression.Operator.NOT);
      ((PrefixExpression) $).setOperand(ie);
    }
    return $;
  }
  @Override String description(final InfixExpression n) {
    final Expression e = ((MethodInvocation) n.getLeftOperand()).getExpression();
    return e == null ? "Use isEmpty()" : "Use " + e.toString() + ".isEmpty()";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}