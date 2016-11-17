package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class AveragingMetricalAnalyzer extends MetricalAnalyzer<List<Int>> {
  @Override protected abstract int metric(ASTNode n);

  @SuppressWarnings("boxing") @Override public void logMethod(final MethodDeclaration before, final ASTNode after) {
    int statements = metrics.countStatements(before);
    getSafe(beforeHistogram, statements).add(Int.valueOf(metric(before)));
    getSafe(afterHistogram, statements).add(Int.valueOf(metric(after)));
  }

  private static List<Int> getSafe(final Map<Integer, List<Int>> m, final Integer i) {
    m.putIfAbsent(i, new ArrayList<>());
    return m.get(i);
  }

  @Override protected double enumElement(List<Int> is) {
    return 1.0 * is.stream().reduce((x, y) -> Int.valueOf(x.inner + y.inner)).get().inner / is.size();
  }
}
