package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.name;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Converts {@code x.size()==0} to {@code x.isEmpty()}, {@code x.size()!=0 }
 * and {@code x.size()>=1} {@code !x.isEmpty()}, {@code x.size()<0} to
 * {@code false}, and {@code x.size()>=0} to {@code true}.
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon <code><shalmon.niv [at] gmail.com></code>
 * @author Stav Namir <code><stav1472 [at] gmail.com></code>
 * @since 2016-04-24 */
public final class InfixComparisonSizeToZero extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -4217296742524813844L;

  @NotNull private static String description(@Nullable final Expression ¢) {
    return "Use " + (¢ != null ? ¢ + "" : "isEmpty()");
  }

  @Nullable private static ASTNode replacement(final Operator o, @NotNull final Expression receiver, final int threshold) {
    assert receiver != null : fault.dump() + //
        "\n threshold='" + threshold + //
        "\n receiver ='" + receiver + //
        "\n operator ='" + o + //
        fault.done();
    final MethodInvocation $ = subject.operand(receiver).toMethod("isEmpty");
    assert $ != null : "All I know is that threshould=" + threshold + ", receiver = " + $ + ", and o=" + o;
    return replacement(o, threshold, $);
  }

  private static ASTNode replacement(final Operator o, final int threshold, @NotNull final MethodInvocation $) {
    if (o == GREATER_EQUALS)
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
    assert fault.unreachable() : fault.dump() + //
        "\n threshold='" + threshold + //
        "\n operator ='" + o + //
        fault.done();
    return null;
  }

  @Nullable private static ASTNode replacement(final Operator o, final int sign, @NotNull final NumberLiteral l, @NotNull final Expression receiver) {
    return replacement(o, receiver, sign * Integer.parseInt(l.getToken()));
  }

  private static ASTNode replacement(final Operator o, @NotNull final MethodInvocation i, final Expression x) {
    if (!"size".equals(name(i).getIdentifier()))
      return null;
    int $ = -1;
    @Nullable NumberLiteral l = az.throwing.negativeLiteral(x);
    if (l == null) {
      /* should be unnecessary since validTypes uses isNumber so n is either a
       * NumberLiteral or an PrefixExpression which is a negative number */
      l = az.numberLiteral(x);
      if (l == null)
        return null;
      $ = 1;
    }
    @NotNull final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    /* In case binding is available, uses it to ensure that isEmpty() is
     * accessible from current scope. Currently untested */
    if (i.getAST().hasResolvedBindings()) {
      @Nullable final CompilationUnit u = containing.compilationUnit(x);
      if (u == null)
        return null;
      @Nullable final IMethodBinding b = BindingUtils.getVisibleMethod(receiver.resolveTypeBinding(), "isEmpty", null, x, u);
      if (b == null)
        return null;
      final ITypeBinding t = b.getReturnType();
      if (!"boolean".equals(t + "") && !"java.lang.Boolean".equals(t.getBinaryName()))
        return null;
    }
    return replacement(o, $, l, receiver);
  }

  private static boolean validTypes(@NotNull final Expression ¢1, @NotNull final Expression ¢2) {
    return iz.pseudoNumber(¢1) && iz.methodInvocation(¢2) //
        || iz.pseudoNumber(¢2) && iz.methodInvocation(¢1);
  }

  @NotNull @Override public String description(final InfixExpression ¢) {
    @NotNull final Expression $ = left(¢);
    return description(expression($ instanceof MethodInvocation ? $ : right(¢)));
  }

  @Override public ASTNode replacement(@NotNull final InfixExpression x) {
    final Operator $ = x.getOperator();
    if (!iz.comparison($))
      return null;
    @NotNull final Expression right = right(x), left = left(x);
    return !validTypes(right, left) ? null
        : iz.methodInvocation(left) ? replacement($, az.methodInvocation(left), right)
            : replacement(wizard.conjugate($), az.methodInvocation(right), left);
  }
}
