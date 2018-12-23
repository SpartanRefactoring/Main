package il.org.spartan.spartanizer.tippers.junit;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** @author Dor Ma'ayan
 * @since 2018-12-19 */
public class AssertEqualsToAssert extends ReplaceCurrentNode<MethodInvocation> implements Category.Idiomatic {
  private static final long serialVersionUID = 1L;

  public static boolean arePrimitives(final Expression e1, final Expression e2) {
    return type.isDouble(e1) || type.isInt(e1) || type.isLong(e1) || //
        type.isDouble(e2) || type.isInt(e2) || type.isLong(e2);
  }
  @Override public ASTNode replacement(final MethodInvocation n) {
    if (n.getName().getIdentifier().equals("assertEquals")) {
      final List<Expression> args = extract.methodInvocationArguments(n);
      final AssertStatement a = n.getAST().newAssertStatement();
      if (args.size() == 2) {
        if (arePrimitives(args.get(0), args.get(1))) {
          final InfixExpression e = a.getAST().newInfixExpression();
          e.setOperator(Operator.EQUALS);
          e.setLeftOperand(copy.of(args.get(0)));
          e.setRightOperand(copy.of(args.get(1)));
          a.setExpression(e);
          return a;
        }
        final MethodInvocation e = a.getAST().newMethodInvocation();
        e.setExpression(copy.of(args.get(0)));
        e.setName(e.getAST().newSimpleName("equals"));
        e.arguments().add(copy.of(args.get(1)));
        a.setExpression(e);
        return a;
      }
      if (args.size() == 3) {
        if (arePrimitives(args.get(1), args.get(2))) {
          final InfixExpression e = a.getAST().newInfixExpression();
          e.setOperator(Operator.EQUALS);
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
        a.setExpression(e);
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
