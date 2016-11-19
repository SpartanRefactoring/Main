package il.org.spartan.spartanizer.research.analyses;

import java.text.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class MetricalAnalyzer<T> {
  Map<Integer, T> beforeHistogram = new HashMap<>();
  Map<Integer, T> afterHistogram = new HashMap<>();

  protected abstract int metric(ASTNode n);

  public abstract void logMethod(final MethodDeclaration before, final ASTNode after);

  public void print() {
    System.out.println("[before]");
    printMap(beforeHistogram);
    System.out.println("[After]");
    printMap(afterHistogram);
  }

  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("boxing") public void printComparison() {
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
  @SuppressWarnings("boxing") public void printAccumulated() {
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

  private int getMax(final Map<Integer, T> i) {
    return i.keySet().stream().max((x, y) -> x.intValue() > y.intValue() ? 1 : -1).get().intValue();
  }

  private void printMap(final Map<Integer, T> i) {
    for (final Integer k : i.keySet())
      System.out.println(k.intValue() + " : " + tidy(enumElement(i.get(k))));
  }

  /** If double is integer, removes the .0
   * @param ¢
   * @return */
  public static String tidy(final double ¢) {
    int a = 0;
    final double ¢formatted = Double.parseDouble(new DecimalFormat("#0.00").format(¢));
    if (¢formatted != Math.floor(¢formatted))
      return ¢formatted + "";
    a = (int) ¢formatted;
    return a + "";
  }

  protected static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }

  protected abstract double enumElement(T t);
}
