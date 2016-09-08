package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** Converts <code>x.size()==0</code> to <code>x.isEmpty()</code>,
 * <code>x.size()!=0 </code> and <code>x.size()>=1</code>
 * <code>!x.isEmpty()</code>, <code>x.size()<0</code> to <code><b>false</b>,and
 * <code>x.size()>=0</code> to <code><b>true</b>.
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @author Yossi Gil
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon <code><shalmon.niv [at] gmail.com></code>
 * @author Stav Namir <code><stav1472 [at] gmail.com></code>
 * @since 2016-04-24 */
public final class InfixComparisonSizeToZero extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  private static String description(final Expression x) {
    return x == null ? "Use isEmpty()" : "Use " + x + ".isEmpty()";
  }

  private static NumberLiteral getNegativeNumber(final Expression ¢) {
    return !(¢ instanceof PrefixExpression) ? null : getNegativeNumber((PrefixExpression) ¢);
  }

  private static NumberLiteral getNegativeNumber(final PrefixExpression ¢) {
    return ¢.getOperator() != PrefixExpression.Operator.MINUS || !(¢.getOperand() instanceof NumberLiteral) ? null : (NumberLiteral) ¢.getOperand();
  }

  private static boolean isNumber(final Expression ¢) {
    return ¢ instanceof NumberLiteral || getNegativeNumber(¢) != null;
  }

  private static ASTNode replacement(final Operator o, final int threshold, final MethodInvocation $) {
    if (o == Operator.GREATER_EQUALS)
      return replacement(GREATER, threshold - 1, $);
    if (o == LESS_EQUALS)
      return replacement(LESS, threshold + 1, $);
    final AST ast = $.getAST();
    if (threshold < 0)
      return ast.newBooleanLiteral(!in(o, EQUALS, LESS));
    if (o == EQUALS)
      return threshold == 0 ? $ : null;
    if (o == NOT_EQUALS || o == GREATER)
      return threshold != 0 ? null : make.notOf($);
    if (o == LESS)
      return threshold == 0 ? ast.newBooleanLiteral(false) : threshold != 1 ? null : $;
    assert false : o + ": uncrecognized";
    return null;
  }

  private static ASTNode replacement(final Operator o, final int sign, final NumberLiteral l, final Expression receiver) {
    return replacement(o, sign * Integer.parseInt(l.getToken()), subject.operand(receiver).toMethod("isEmpty"));
  }

  private static ASTNode replacement(final Operator o, final MethodInvocation i, final Expression x) {
    if (!"size".equals(step.name(i).getIdentifier()))
      return null;
    int sign = -1;
    NumberLiteral l = getNegativeNumber(x);
    if (l == null) {
      /* should be unnecessary since validTypes uses isNumber so n is either a
       * NumberLiteral or an PrefixExpression which is a negative number */
      l = az.numberLiteral(x);
      if (l == null)
        return null;
      sign = 1;
    }
    final Expression receiver = step.receiver(i);
    /* In case binding is available, uses it to ensure that isEmpty() is
     * accessible from current scope. Currently untested */
    if (i.getAST().hasResolvedBindings()) {
      final CompilationUnit u = hop.compilationUnit(x);
      if (u == null)
        return null;
      final IMethodBinding b = BindingUtils.getVisibleMethod(receiver == null ? BindingUtils.container(x) : receiver.resolveTypeBinding(), "isEmpty",
          null, x, u);
      if (b == null)
        return null;
      final ITypeBinding t = b.getReturnType();
      if (!"boolean".equals(t + "") && !"java.lang.Boolean".equals(t.getBinaryName()))
        return null;
    }
    return replacement(o, sign, l, receiver);
  }

  private static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return isNumber(¢1) && iz.methodInvocation(¢2) //
        || isNumber(¢2) && iz.methodInvocation(¢1);
  }

  @Override String description(final InfixExpression x) {
    final Expression right = step.right(x);
    final Expression left = step.left(x);
    return description(left instanceof MethodInvocation ? left : right);
  }

  @Override ASTNode replacement(final InfixExpression x) {
    final Operator o = x.getOperator();
    if (!iz.comparison(o))
      return null;
    final Expression right = step.right(x);
    final Expression left = step.left(x);
    return !validTypes(right, left) ? null
        : iz.methodInvocation(left) ? //
            replacement(o, az.methodInvocation(left), right) //
            : replacement(wizard.conjugate(o), az.methodInvocation(right), left)//
    ;
  }
}
