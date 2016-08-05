package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.BindingUtils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to change emptiness check from
 *
 * <pre>
 * x.size() != 0
 * </pre>
 *
 * or
 *
 * <pre>
 * x.size() > 0
 * </pre>
 *
 * or
 *
 * <pre>
 * x.size() >= 1
 * </pre>
 *
 * to
 *
 * <pre>
 * !x.isEmpty()
 * </pre>
 *
 * .
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24 */
public class CollectionZeroSize extends Wring.ReplaceCurrentNode<InfixExpression> {
  @Override ASTNode replacement(final InfixExpression e) {
    if (!e.getAST().hasResolvedBindings())
      return null;
    final Operator o = e.getOperator();
    if (!isComparison(o))
      return null;
    final Expression right = right(e);
    final Expression left = left(e);
    if (left instanceof MethodInvocation == right instanceof MethodInvocation)
      return null;
    if (left instanceof NumberLiteral == right instanceof NumberLiteral)
      return null;
    if (left instanceof MethodInvocation)
      return replacement(e, o, (MethodInvocation) left, (NumberLiteral) right);
    return replacement(e, conjugate(o), (MethodInvocation) right, (NumberLiteral) left);
  }
  private static ASTNode replacement(final InfixExpression e, final Operator o, final MethodInvocation i, final NumberLiteral l) {
    if (!"size".equals(i.getName().getIdentifier()) || Double.parseDouble(l.getToken()) != 0)
      return null;
    final CompilationUnit u = extract.compilationUnit(e);
    if (u == null)
      return null;
    final Expression receiver = i.getExpression();
    final IMethodBinding b = getVisibleMethod(receiver != null ? receiver.resolveTypeBinding() : container(e), "isEmpty", null, e, u);
    if (b == null)
      return null;
    final ITypeBinding t = b.getReturnType();
    if (b == null || !"boolean".equals("" + t) && !"java.lang.Boolean".equals(t.getBinaryName()))
      return null;
    final MethodInvocation $ = Subject.operand(receiver).toMethod("isEmpty");
    if (o.equals(InfixExpression.Operator.EQUALS))
      return $;
    return Subject.operand($).to(NOT);
  }
  @Override String description(final InfixExpression n) {
    final Expression e = ((MethodInvocation) n.getLeftOperand()).getExpression();
    return e == null ? "Use isEmpty()" : "Use " + e.toString() + ".isEmpty()";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}