package il.org.spartan.refactoring.wring;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.BindingUtils;

/**
 * A {@link Wring} to change emptiness check from
 * <code><pre>x.size() != 0</pre></code> or <code><pre>x.size() > 0</pre></code>
 * to <code><pre>!x.isEmpty()</pre></code>. TODO add tests
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24
 */
public class CollectionZeroSize extends Wring.ReplaceCurrentNode<InfixExpression> {
  // list of accepted operators
  final List<InfixExpression.Operator> ao = Arrays.asList(
      new Operator[] { InfixExpression.Operator.EQUALS, InfixExpression.Operator.NOT_EQUALS, InfixExpression.Operator.GREATER });

  @Override ASTNode replacement(final InfixExpression n) {
    if (!n.getAST().hasResolvedBindings() || !(n.getLeftOperand() instanceof MethodInvocation)
        || !(n.getRightOperand() instanceof NumberLiteral) || !ao.contains(n.getOperator()))
      return null;
    final MethodInvocation mi = (MethodInvocation) n.getLeftOperand();
    final NumberLiteral nl = (NumberLiteral) n.getRightOperand();
    if (!mi.getName().getIdentifier().equals("size") || Double.parseDouble(nl.getToken()) != 0)
      return null;
    final Expression mie = mi.getExpression();
    final IMethodBinding iemb = BindingUtils.getVisibleMethod(mie != null ? mie.resolveTypeBinding() : BindingUtils.getClass(n),
        "isEmpty", null, n, u);
    if (iemb == null
        || !(iemb.getReturnType().toString().equals("boolean") || iemb.getReturnType().getBinaryName().equals("java.lang.Boolean")))
      return null;
    final MethodInvocation ie = n.getAST().newMethodInvocation();
    ie.setExpression((Expression) ASTNode.copySubtree(n.getAST(), mi.getExpression()));
    ie.setName(n.getAST().newSimpleName("isEmpty"));
    ASTNode $;
    if (n.getOperator().equals(InfixExpression.Operator.EQUALS))
      $ = ie;
    else {
      $ = n.getAST().newPrefixExpression();
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
    // TODO maybe change WringGroup
    return WringGroup.DISCARD_METHOD_INVOCATION;
  }
}