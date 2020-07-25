package il.org.spartan.spartanizer.tippers.junit;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** @author Dor Ma'ayan
 * @since 2018-12-19 */
public class AssertNotEqualsToAssert extends ReplaceCurrentNode<MethodInvocation> implements Category.Idiomatic {
  private static final long serialVersionUID = 1L;

  public static boolean arePrimitives(final Expression e1, final Expression e2) {
    return type.isDouble(e1) || type.isInt(e1) || type.isLong(e1) || //
        type.isDouble(e2) || type.isInt(e2) || type.isLong(e2);
  }
  @Override public ASTNode replacement(final MethodInvocation n) {
    String assertion = n.getName().getIdentifier();
    if (assertion.equals("assertNotEquals") || assertion.equals("assertNotSame")) {
      final List<Expression> args = extract.methodInvocationArguments(n);
      final AssertStatement a = n.getAST().newAssertStatement();
      if (args.size() == 2) {
        if (arePrimitives(args.get(0), args.get(1)) || assertion.equals("assertNotSame")) {
          final InfixExpression e = a.getAST().newInfixExpression();
          e.setOperator(Operator.NOT_EQUALS);
          e.setLeftOperand(copy.of(args.get(0)));
          e.setRightOperand(copy.of(args.get(1)));
          a.setExpression(e);
          return a;
        }
        final MethodInvocation e = a.getAST().newMethodInvocation();
        e.setExpression(copy.of(args.get(0)));
        e.setName(e.getAST().newSimpleName("equals"));
        e.arguments().add(copy.of(args.get(1)));
        a.setExpression(cons.not(e));
        return a;
      }
      if (args.size() == 3) {
        if (arePrimitives(args.get(1), args.get(2)) || assertion.equals("assertNotSame")) {
          final InfixExpression e = a.getAST().newInfixExpression();
          e.setOperator(Operator.NOT_EQUALS);
          e.setLeftOperand(copy.of(args.get(1)));
          e.setRightOperand(copy.of(args.get(2)));
          a.setExpression(e);
          a.setMessage(copy.of(args.get(0)));
          return a;
        }
        final MethodInvocation e = a.getAST().newMethodInvocation();
        e.setExpression(copy.of(args.get(1)));
        e.setName(e.getAST().newSimpleName("equals"));
        e.arguments().add(copy.of(args.get(2)));
        a.setExpression(cons.not(e));
        a.setMessage(copy.of(args.get(0)));
        return a;
      }
    }
    return null;
  }
  @Override public String description(final MethodInvocation n) {
    return "Replace";
  }
}
