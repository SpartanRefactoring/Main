package il.org.spartan.spartanizer.tippers.junit;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Dor Ma'ayan
 * @since 2018-12-19 */
public class AssertTrueFalseToAssert extends ReplaceCurrentNode<MethodInvocation> {
  private static final long serialVersionUID = 1L;

  @Override public ASTNode replacement(MethodInvocation n) {
    if (n.getName().getIdentifier().equals("assertTrue")) {
      AssertStatement a = n.getAST().newAssertStatement();
      List<Expression> args = extract.methodInvocationArguments(n);
      if (args.size() == 1) {
        a.setExpression(copy.of(args.get(0)));
        return a;
      }
    }
    return null;
  }
  @Override public String description(MethodInvocation n) {
    return null;
  }
}
