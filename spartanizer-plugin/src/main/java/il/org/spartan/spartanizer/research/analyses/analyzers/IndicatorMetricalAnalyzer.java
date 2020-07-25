package il.org.spartan.spartanizer.research.analyses.analyzers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.util.measure;
import il.org.spartan.utils.Int;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class IndicatorMetricalAnalyzer extends Analyzer<List<Int>> {
  final Map<Integer, List<Int>> histogram = new HashMap<>();

  @Override protected abstract int metric(ASTNode n);
  @Override @SuppressWarnings("boxing") public void logMethod(final MethodDeclaration before, final MethodDeclaration after) {
    getSafe(histogram, measure.commands(before)).add(Int.valueOf(as.bit(metric(before) >= metric(after))));
  }
  private static Collection<Int> getSafe(final Map<Integer, List<Int>> m, final Integer i) {
    m.putIfAbsent(i, an.empty.list());
    return m.get(i);
  }
  @Override @SuppressWarnings("boxing") public void printComparison() {
    final int max = getMax(histogram);
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (histogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + tidy(enumElement(histogram.get(¢))));
  }
  @Override public void printAccumulated() {
    //
  }
}
