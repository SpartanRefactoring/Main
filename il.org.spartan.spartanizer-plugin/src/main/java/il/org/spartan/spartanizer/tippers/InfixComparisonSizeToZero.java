package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Converts {@code x.size()==0} to {@code x.isEmpty()}, {@code x.size()!=0 }
 * and {@code x.size()>=1} {@code !x.isEmpty()}, {@code x.size()<0} to
 * {@code false}, and {@code x.size()>=0} to {@code true}.
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @author Yossi Gil
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon <code><shalmon.niv [at] gmail.com></code>
 * @author Stav Namir <code><stav1472 [at] gmail.com></code>
 * @since 2016-04-24 */
public final class InfixComparisonSizeToZero extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = -0x3A86D90B0A9A8214L;

  private static String description(final Expression ¢) {
    return "Use " + (¢ != null ? ¢ + "" : "isEmpty()");
  }
  private static ASTNode replacement(final Operator o, final Expression receiver, final int threshold) {
    assert receiver != null : fault.dump() + //
        "\n threshold='" + threshold + //
        "\n receiver ='" + receiver + //
        "\n operator ='" + o + //
        fault.done();
    final MethodInvocation ret = subject.operand(receiver).toMethod("isEmpty");
    assert ret != null : "All I know is that threshould=" + threshold + ", receiver = " + ret + ", and o=" + o;
    return replacement(o, threshold, ret);
  }
  private static ASTNode replacement(final Operator o, final int threshold, final MethodInvocation $) {
    if (o == GREATER_EQUALS)
      return replacement(GREATER, threshold - 1, $);
    if (o == LESS_EQUALS)
      return replacement(LESS, threshold + 1, $);
    final AST ret = $.getAST();
    if (threshold < 0)
      return ret.newBooleanLiteral(!in(o, EQUALS, LESS));
    if (o == EQUALS)
      return threshold == 0 ? $ : null;
    if (o == NOT_EQUALS || o == GREATER)
      return threshold != 0 ? null : make.notOf($);
    if (o == LESS)
      return threshold == 0 ? ret.newBooleanLiteral(false) : threshold != 1 ? null : $;
    assert fault.unreachable() : fault.dump() + //
        "\n threshold='" + threshold + //
        "\n operator ='" + o + //
        fault.done();
    return null;
  }
  private static ASTNode replacement(final Operator o, final int sign, final NumberLiteral l, final Expression receiver) {
    return replacement(o, receiver, sign * Integer.parseInt(l.getToken()));
  }
  private static ASTNode replacement(final Operator o, final MethodInvocation i, final Expression x) {
    if (!"size".equals(name(i).getIdentifier()))
      return null;
    int ret = -1;
    NumberLiteral l = az.throwing.negativeLiteral(x);
    if (l == null) {
      l = az.numberLiteral(x);
      if (l == null)
        return null;
      ret = 1;
    }
    final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    if (!i.getAST().hasResolvedBindings())
      return replacement(o, ret, l, receiver);
    final CompilationUnit u = containing.compilationUnit(x);
    if (u == null)
      return null;
    final IMethodBinding b = BindingUtils.getVisibleMethod(receiver.resolveTypeBinding(), "isEmpty", null, x, u);
    if (b == null)
      return null;
    final ITypeBinding t = b.getReturnType();
    return !"boolean".equals(t + "") && !"java.lang.Boolean".equals(t.getBinaryName()) ? null : replacement(o, ret, l, receiver);
  }
  private static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return iz.pseudoNumber(¢1) && iz.methodInvocation(¢2) //
        || iz.pseudoNumber(¢2) && iz.methodInvocation(¢1);
  }
  @Override public String description(final InfixExpression ¢) {
    final Expression ret = left(¢);
    return description(expression(ret instanceof MethodInvocation ? ret : right(¢)));
  }
  @Override public ASTNode replacement(final InfixExpression x) {
    final Operator ret = x.getOperator();
    if (!iz.comparison(ret))
      return null;
    final Expression right = right(x), left = left(x);
    return !validTypes(right, left) ? null
        : iz.methodInvocation(left) ? replacement(ret, az.methodInvocation(left), right)
            : replacement(op.conjugate(ret), az.methodInvocation(right), left);
  }
}
