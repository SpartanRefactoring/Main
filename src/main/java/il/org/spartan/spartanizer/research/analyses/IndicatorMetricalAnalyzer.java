package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** Class for averaging whatever about methods before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class IndicatorMetricalAnalyzer extends Analyzer<List<Int>> {
  final Map<Integer, List<Int>> histogram = new HashMap<>();

  @Override protected abstract int metric(ASTNode n);

  @Override @SuppressWarnings("boxing") public void logMethod(final MethodDeclaration before, final MethodDeclaration after) {
    getSafe(histogram, measure.statements(before)).add(Int.valueOf(metric(before) < metric(after) ? 0 : 1));
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
