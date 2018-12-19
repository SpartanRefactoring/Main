package il.org.spartan.spartanizer.tippers.junit;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan
 * @since 2018-12-19 */
public class AssertEqualsToAssert extends ReplaceCurrentNode<MethodInvocation> {
  private static final long serialVersionUID = 1L;

  public boolean arePrimitives(Expression e1, Expression e2) {
    return type.isDouble(e1) || type.isInt(e1) || type.isLong(e1) || //
        type.isDouble(e2) || type.isInt(e2) || type.isLong(e2);
  }
  @Override public ASTNode replacement(MethodInvocation n) {
    if (n.getName().getIdentifier().equals("assertEquals")) {
      AssertStatement a = n.getAST().newAssertStatement();
      List<Expression> args = extract.methodInvocationArguments(n);
      if (args.size() == 2) {
        if (arePrimitives(args.get(0), args.get(1))) {
          InfixExpression e = a.getAST().newInfixExpression();
          e.setOperator(Operator.EQUALS);
          e.setLeftOperand(copy.of(args.get(0)));
          e.setRightOperand(copy.of(args.get(1)));
          a.setExpression(e);
          return a;
        }
        MethodInvocation e = a.getAST().newMethodInvocation();
        e.setExpression(copy.of(args.get(0)));
        e.setName(e.getAST().newSimpleName("equals"));
        e.arguments().add(copy.of(args.get(1)));
        a.setExpression(e);
        return a;
      }
    }
    return null;
  }
  @Override public String description(MethodInvocation n) {
    return null;
  }
}
