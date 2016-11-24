package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class IndicatorMetricalAnalyzer extends Analyzer<List<Int>> {
  Map<Integer, List<Int>> histogram = new HashMap<>();

  @Override protected abstract int metric(ASTNode n);

  @Override @SuppressWarnings("boxing") public void logMethod(final MethodDeclaration before, final MethodDeclaration after) {
    getSafe(histogram, metrics.countStatements(before)).add(Int.valueOf(metric(before) < metric(after) ? 0 : 1));
  }

  private static List<Int> getSafe(final Map<Integer, List<Int>> m, final Integer i) {
    m.putIfAbsent(i, new ArrayList<>());
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
