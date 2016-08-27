package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
//import static il.org.spartan.refactoring.utils.Is.*;
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
 * or
 * <pre>
 * x.size() &gt; 0
 * </pre>
 * or
 * <pre>
 * x.size() == 1
 * </pre>
 * or
 * <pre>
 * x.size() == 2
 * </pre>
 * to
 * <pre>
 * !x.isEmpty()
 * </pre>
 * _____________
 * <pre>
 * x.size() == 0
 * </pre>
 * to
 * <pre>
 * x.isEmpty()
 * </pre>
 * _____________
 * <pre>
 * x.size() &gt;= 0
 * </pre>
 * to
 * <pre>
 * true
 * </pre>
 * _____________
 * <pre>
 * x.size() &lt; 0
 * </pre>
 * to
 * <pre>
 * false
 * </pre>
 *
 * .
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon <code><shalmon.niv [at] gmail.com></code>
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @author Stav Namir <code><stav1472 [at] gmail.com></code>
 * @since 2016-04-24 */
public final class InfixComparisonSizeToZero extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  static boolean validTypes(final Expression ¢1, final Expression ¢2) {
    return (¢2 instanceof MethodInvocation && ¢1 instanceof NumberLiteral) //
        || (¢2 instanceof NumberLiteral && ¢1 instanceof MethodInvocation);
  }

  @SuppressWarnings("fallthrough") private static ASTNode replacement(final InfixExpression e, final Operator o, final MethodInvocation i,
      final NumberLiteral l) {
    /* final CompilationUnit u = compilationUnit(e); if (u == null) return
     * null; */
    final Expression receiver = receiver(i);
    /* final IMethodBinding b = BindingUtils.getVisibleMethod(receiver == null ?
     * BindingUtils.container(e) : receiver.resolveTypeBinding(), "isEmpty",
     * null, e, u); if (b == null) return null; final ITypeBinding t =
     * b.getReturnType(); if (!"boolean".equals("" + t) &&
     * !"java.lang.Boolean".equals(t.getBinaryName())) return null; final
     * MethodInvocation $ = subject.operand(receiver).toMethod("isEmpty");
     * return o.equals(InfixExpression.Operator.EQUALS) ? $ :
     * subject.operand($).to(NOT); */ // The original case assumes there is
                                      // Binding
    final MethodInvocation $ = subject.operand(receiver).toMethod("isEmpty");
    int number = Integer.parseInt(l.getToken());
    switch (o.toString()) {
      case "==":
        if (number == 0)
          return $;
        if (number == 1 || number == 2)
          return subject.operand($).to(NOT);
        return null;
      case "!=":
        if (number == 0)
          return subject.operand($).to(NOT);
        return null;
      case ">":
        if (number == 0)
          return subject.operand($).to(NOT);
      case ">=":
        if (number <= 0)
          return e.getAST().newBooleanLiteral(true);
      case "<=":
        if (number == 0)
          return $;
      case "<":
        if (number >= 0)
          return  e.getAST().newBooleanLiteral(false);
      default:
        return null;
    }
  }

  private static String descriptionAux(final Expression e) {
    return e == null ? "Use isEmpty()" : "Use " + e + ".isEmpty()";
  }

  @Override String description(final InfixExpression e) {
    final Expression right = right(e);
    final Expression left = left(e);
    return left instanceof MethodInvocation ? descriptionAux(left) : descriptionAux(right);
  }

  @Override ASTNode replacement(final InfixExpression e) {
    /* if (!e.getAST().hasResolvedBindings()) return null; */ // Yossi Told To
                                                              // Remove That For
                                                              // Tests
    final Operator o = e.getOperator();
    final Expression right = right(e);
    final Expression left = left(e);
    return left instanceof MethodInvocation ? //
        replacement(e, o, (MethodInvocation) left, (NumberLiteral) right) //
        : replacement(e, conjugate(o), (MethodInvocation) right, (NumberLiteral) left)//
    ;
  }

  @Override boolean scopeIncludes(final InfixExpression e) {
    final Operator o = e.getOperator();
    if (!Is.isComparison(o))
      return false;
    final Expression right = right(e);
    final Expression left = left(e);
    if (!validTypes(right, left)) {
      return false;
    }
    return left instanceof MethodInvocation ? "size".equals(name((MethodInvocation) left).getIdentifier())
        : "size".equals(name((MethodInvocation) right).getIdentifier());
  }
}
