package il.org.spartan.spartanizer.research.linguistic;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-09 */
@UnderConstruction
public class LinguisticHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent e) {
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
    ExpressionStatement es = az.expressionStatement(
        StreamSupport.stream(ancestors.of(n).spliterator(), false).filter(p -> p instanceof ExpressionStatement).findFirst().orElse(null));
    if (es == null)
      es = az.expressionStatement(
          StreamSupport.stream(descendants.of(n).spliterator(), false).filter(p -> p instanceof ExpressionStatement).findFirst().orElse(null));
    return es == null ? null : fapi(es);
  }
  private static FAPI fapi(ExpressionStatement s) {
    final Expression e = s.getExpression();
    if (!iz.methodInvocation(e) && !iz.fieldAccess(e))
      return null;
    Name name = null;
    final List<Expression> invocations = new ArrayList<>();
    for (Expression x = e; x != null; x = iz.fieldAccess(x) ? az.fieldAccess(x).getExpression()
        : iz.methodInvocation(x) ? az.methodInvocation(x).getExpression() : null) {
      invocations.add(0, x);
      if (iz.fieldAccess(x) && (name = az.name(az.fieldAccess(x).getExpression())) != null
          || iz.methodInvocation(x) && (name = az.name(az.methodInvocation(x).getExpression())) != null)
        break;
    }
    return !invocations.stream().anyMatch(x -> iz.methodInvocation(x)) ? null //
        : new FAPI(s.getAST(), name, invocations).solveBinding().fixPath();
  }
}
