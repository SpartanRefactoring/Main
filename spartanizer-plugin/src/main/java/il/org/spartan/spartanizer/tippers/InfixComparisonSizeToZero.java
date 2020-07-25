package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.receiver;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.GREATER_EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS_EQUALS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.NOT_EQUALS;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.containing;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.BindingUtils;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.fault;

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
    final MethodInvocation $ = subject.operand(receiver).toMethod("isEmpty");
    assert $ != null : "All I know is that threshould=" + threshold + ", receiver = " + $ + ", and o=" + o;
    return replacement(o, threshold, $);
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
      return threshold != 0 ? null : cons.not($);
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
    int $ = -1;
    NumberLiteral l = az.throwing.negativeLiteral(x);
    if (l == null) {
      l = az.numberLiteral(x);
      if (l == null)
        return null;
      $ = 1;
    }
    final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    if (!i.getAST().hasResolvedBindings())
      return replacement(o, $, l, receiver);
    final CompilationUnit u = containing.compilationUnit(x);
    if (u == null)
      return null;
    final IMethodBinding b = BindingUtils.getVisibleMethod(receiver.resolveTypeBinding(), "isEmpty", null, x, u);
    if (b == null)
      return null;
    final ITypeBinding t = b.getReturnType();
    return !"boolean".equals(t + "") && !"java.lang.Boolean".equals(t.getBinaryName()) ? null : replacement(o, $, l, receiver);
  }
  private static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return iz.pseudoNumber(¢1) && iz.methodInvocation(¢2) //
        || iz.pseudoNumber(¢2) && iz.methodInvocation(¢1);
  }
  @Override public String description(final InfixExpression ¢) {
    final Expression $ = left(¢);
    return description(expression($ instanceof MethodInvocation ? $ : right(¢)));
  }
  @Override public ASTNode replacement(final InfixExpression x) {
    final Operator $ = x.getOperator();
    if (!iz.comparison($))
      return null;
    final Expression right = right(x), left = left(x);
    return !validTypes(right, left) ? null
        : iz.methodInvocation(left) ? replacement($, az.methodInvocation(left), right)
            : replacement(op.conjugate($), az.methodInvocation(right), left);
  }
}
