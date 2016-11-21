package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class MetricalAnalyzer<T> extends Analyzer<T> {
  Map<Integer, T> beforeHistogram = new HashMap<>();
  Map<Integer, T> afterHistogram = new HashMap<>();

  public void print() {
    System.out.println("[before]");
    printMap(beforeHistogram);
    System.out.println("[After]");
    printMap(afterHistogram);
  }

  /** [[SuppressWarningsSpartan]] */
  @Override @SuppressWarnings("boxing") public void printComparison() {
    final int max1 = getMax(beforeHistogram);
    final int max2 = getMax(afterHistogram);
    final int max = max1 > max2 ? max1 : max2;
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + tidy(beforeHistogram.containsKey(¢) ? enumElement(beforeHistogram.get(¢)) : 0) + " -> "
            + tidy(afterHistogram.containsKey(¢) ? enumElement(afterHistogram.get(¢)) : 0));
  }

  /** [[SuppressWarningsSpartan]] */
  @Override @SuppressWarnings("boxing") public void printAccumulated() {
    final int max1 = getMax(beforeHistogram);
    final int max2 = getMax(afterHistogram);
    final int max = max1 > max2 ? max1 : max2;
    double acc1 = 0;
    double acc2 = 0;
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + tidy(acc1 += beforeHistogram.containsKey(¢) ? enumElement(beforeHistogram.get(¢)) : 0) + " -> "
            + tidy(acc2 += afterHistogram.containsKey(¢) ? enumElement(afterHistogram.get(¢)) : 0));
  }

  private void printMap(final Map<Integer, T> i) {
    for (final Integer k : i.keySet())
      System.out.println(k.intValue() + " : " + tidy(enumElement(i.get(k))));
  }
}
