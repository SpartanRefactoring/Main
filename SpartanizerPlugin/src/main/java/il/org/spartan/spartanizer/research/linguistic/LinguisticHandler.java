package il.org.spartan.spartanizer.research.linguistic;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-09 */
public class LinguisticHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent __) {
    final Selection s = Selection.Util.current();
    if (s == null || !s.isTextSelection)
      return null;
    s.setUseBinding();
    FAPI i = fapi(s);
    if (i == null)
      return null;
    FAPIGenerator.by(i).in(s.inner.get(0).descriptor.getJavaProject()).generateAll();
    return null;
  }
  private static FAPI fapi(Selection s) {
    ASTNode n = Eclipse.coveringNodeByRange(s.inner.get(0).build().compilationUnit, s.textSelection);
    if (n == null) {
      n = Eclipse.coveredNodeByRange(s.inner.get(0).build().compilationUnit, s.textSelection);
      if (n == null)
        return null;
    }
    ExpressionStatement $ = az.expressionStatement(
        StreamSupport.stream(ancestors.of(n).spliterator(), false).filter(λ -> λ instanceof ExpressionStatement).findFirst().orElse(null));
    if ($ == null)
      $ = az.expressionStatement(
          StreamSupport.stream(descendants.of(n).spliterator(), false).filter(λ -> λ instanceof ExpressionStatement).findFirst().orElse(null));
    return $ == null ? null : fapi($);
  }
  private static FAPI fapi(ExpressionStatement s) {
    final Expression e = s.getExpression();
    if (!iz.methodInvocation(e) && !iz.fieldAccess(e))
      return null;
    Name $ = null;
    final List<Expression> invocations = new ArrayList<>();
    for (Expression ¢ = e; ¢ != null; ¢ = iz.fieldAccess(¢) ? az.fieldAccess(¢).getExpression()
        : iz.methodInvocation(¢) ? az.methodInvocation(¢).getExpression() : null) {
      invocations.add(0, ¢);
      if (iz.fieldAccess(¢) && ($ = az.name(az.fieldAccess(¢).getExpression())) != null
          || iz.methodInvocation(¢) && ($ = az.name(az.methodInvocation(¢).getExpression())) != null)
        break;
    }
    return new FAPI(s.getAST(), $, invocations).solveBinding().fixPath();
  }
}
