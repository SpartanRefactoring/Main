package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

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
 * x.size() &gt; 0
 * </pre>
 *
 * or
 *
 * <pre>
 * x.size() &gt;= 1
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
public final class CollectionZeroSize extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  static boolean invalidTypes(final Expression ¢1, final Expression ¢2) {
    return ¢2 instanceof MethodInvocation == ¢1 instanceof MethodInvocation //
        && ¢2 instanceof NumberLiteral == ¢1 instanceof NumberLiteral;
  }
  private static ASTNode replacement(final InfixExpression e, final Operator o, final MethodInvocation i, final NumberLiteral l) {
    if (!"size".equals(name(i).getIdentifier()) || Double.parseDouble(l.getToken()) != 0)
      return null;
    final CompilationUnit u = compilationUnit(e);
    if (u == null)
      return null;
    final Expression receiver = expression(i);
    final IMethodBinding b = BindingUtils.getVisibleMethod(receiver == null ? BindingUtils.container(e) : receiver.resolveTypeBinding(), "isEmpty",
        null, e, u);
    if (b == null)
      return null;
    final ITypeBinding t = b.getReturnType();
    if (!"boolean".equals("" + t) && !"java.lang.Boolean".equals(t.getBinaryName()))
      return null;
    final MethodInvocation $ = subject.operand(receiver).toMethod("isEmpty");
    return o.equals(InfixExpression.Operator.EQUALS) ? $ : subject.operand($).to(NOT);
  }
  @Override String description(final InfixExpression n) {
    final Expression e = ((MethodInvocation) n.getLeftOperand()).getExpression();
    return e == null ? "Use isEmpty()" : "Use " + e + ".isEmpty()";
  }
  @Override ASTNode replacement(final InfixExpression e) {
    if (!e.getAST().hasResolvedBindings())
      return null;
    final Operator o = e.getOperator();
    if (!isComparison(o))
      return null;
    final Expression right = right(e);
    final Expression left = left(e);
    return invalidTypes(right, left) ? null //
        : left instanceof MethodInvocation ? //
            replacement(e, o, (MethodInvocation) left, (NumberLiteral) right) //
            : replacement(e, conjugate(o), (MethodInvocation) right, (NumberLiteral) left)//
    ;
  }
}
