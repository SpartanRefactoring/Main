package il.org.spartan.spartanizer.tippers.junit;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/**  
 * 
 * @author Dor Ma'ayan
 * @since 2018-12-19 */
public class AssertEqualsToAssert extends ReplaceCurrentNode<MethodInvocation> {

  private static final long serialVersionUID = 1L;

  @Override public ASTNode replacement(MethodInvocation n) {
    if(n.getName().getIdentifier().equals("assertEquals")) {
      AssertStatement a = n.getAST().newAssertStatement();
      List<Expression> args = extract.methodInvocationArguments(n);
      if(args.size() == 2) {
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
