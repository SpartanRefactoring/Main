package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class MagicNumbers {
  static Map<Integer, Int> beforeHistogram = new HashMap<>();
  static Map<Integer, Int> afterHistogram = new HashMap<>();

  public static void logMethod(final MethodDeclaration before, final ASTNode after) {
    ++getSafe(beforeHistogram, Integer(count.statements(before))).inner;
    ++getSafe(afterHistogram, Integer(count.statements(after))).inner;
  }
  private static Int getSafe(final Map<Integer, Int> m, final Integer i) {
    m.putIfAbsent(i, new Int());
    return m.get(i);
  }
  private static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }
  public static void print() {
    System.out.println("[before]");
    printMap(beforeHistogram);
    System.out.println("[After]");
    printMap(afterHistogram);
  }
  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("boxing") public static void printComparison() {
    final int max1 = getMax(beforeHistogram);
    final int max2 = getMax(afterHistogram);
    final int max = max1 > max2 ? max1 : max2;
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + (beforeHistogram.containsKey(¢) ? beforeHistogram.get(¢).inner : "0") + " -> "
            + (afterHistogram.containsKey(¢) ? afterHistogram.get(¢).inner : "0"));
  }
  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("boxing") public static void printAccumulated() {
    final int max1 = getMax(beforeHistogram);
    final int max2 = getMax(afterHistogram);
    final int max = max1 > max2 ? max1 : max2;
    int acc1 = 0;
    int acc2 = 0;
    System.out.println();
    for (int ¢ = 0; ¢ < max; ++¢)
      if (beforeHistogram.containsKey(¢) || afterHistogram.containsKey(¢))
        System.out.println("[" + ¢ + "] " + (acc1 += beforeHistogram.containsKey(¢) ? beforeHistogram.get(¢).inner : 0) + " -> "
            + (acc2 += afterHistogram.containsKey(¢) ? afterHistogram.get(¢).inner : 0));
  }
  private static int getMax(final Map<Integer, Int> i) {
    return i.keySet().stream().max((x, y) -> x.intValue() > y.intValue() ? 1 : -1).get().intValue();
  }
  private static void printMap(final Map<Integer, Int> i) {
    for (final Integer k : i.keySet())
      System.out.println(k.intValue() + " : " + i.get(k).inner);
  }
}
