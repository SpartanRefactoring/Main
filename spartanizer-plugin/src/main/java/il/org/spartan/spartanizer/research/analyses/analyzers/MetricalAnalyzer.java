package il.org.spartan.spartanizer.research.analyses.analyzers;

import java.util.HashMap;
import java.util.Map;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class MetricalAnalyzer<T> extends Analyzer<T> {
  final Map<Integer, T> beforeHistogram = new HashMap<>();
  final Map<Integer, T> afterHistogram = new HashMap<>();

  public void print() {
    System.out.println("[before]");
    printMap(beforeHistogram);
    System.out.println("[After]");
    printMap(afterHistogram);
  }
  @Override @SuppressWarnings("boxing") public void printComparison() {
    final int max1 = getMax(beforeHistogram), max2 = getMax(afterHistogram), max = max1 > max2 ? max1 : max2;
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + tidy(!beforeHistogram.containsKey(¢) ? 0 : enumElement(beforeHistogram.get(¢))) + " -> "
            + tidy(!afterHistogram.containsKey(¢) ? 0 : enumElement(afterHistogram.get(¢))));
  }
  @Override @SuppressWarnings("boxing") public void printAccumulated() {
    final int max1 = getMax(beforeHistogram), max2 = getMax(afterHistogram), max = max1 > max2 ? max1 : max2;
    System.out.println();
    double acc1 = 0, acc2 = 0;
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + tidy(acc1 += !beforeHistogram.containsKey(¢) ? 0 : enumElement(beforeHistogram.get(¢))) + " -> "
            + tidy(acc2 += !afterHistogram.containsKey(¢) ? 0 : enumElement(afterHistogram.get(¢))));
  }
  private void printMap(final Map<Integer, T> m) {
    m.keySet().forEach(λ -> System.out.println(λ.intValue() + " : " + tidy(enumElement(m.get(λ)))));
  }
}
