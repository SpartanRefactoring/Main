package il.org.spartan.spartanizer.research.analyses.analyzers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.utils.Int;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class AvgMetricalAnalyzer extends MetricalAnalyzer<List<Int>> {
  @Override protected abstract int metric(ASTNode n);
  @Override @SuppressWarnings("boxing") public void logMethod(final MethodDeclaration before, final MethodDeclaration after) {
    final int statements = Metrics.countStatements(before);
    getSafe(beforeHistogram, statements).add(Int.valueOf(metric(before)));
    getSafe(afterHistogram, statements).add(Int.valueOf(metric(findFirst.instanceOf(MethodDeclaration.class).in(after))));
    if (metric(before) >= metric(findFirst.instanceOf(MethodDeclaration.class).in(after)))
      return;
    System.out.println(metric(before) + " : " + metric(findFirst.instanceOf(MethodDeclaration.class).in(after)));
    System.out.println("****************OMG anomaly***************");
    System.out.println(before);
    System.out.println(findFirst.instanceOf(MethodDeclaration.class).in(after));
    System.out.println("****************   Finito  ***************");
  }
  private static Collection<Int> getSafe(final Map<Integer, List<Int>> m, final Integer i) {
    m.putIfAbsent(i, an.empty.list());
    return m.get(i);
  }
  @Override protected double enumElement(final List<Int> is) {
    return 1.0 * is.stream().reduce((x, y) -> Int.valueOf(x.inner + y.inner)).get().inner / is.size();
  }
}
