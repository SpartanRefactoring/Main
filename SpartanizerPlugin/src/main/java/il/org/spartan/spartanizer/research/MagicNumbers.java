package il.org.spartan.spartanizer.research;

import java.util.*;

import il.org.spartan.spartanizer.utils.*;

/** Class to count statement inside a method before and after refactoring +
 * patterning
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class MagicNumbers {
  static Map<Integer, Int> beforeHistogram = new HashMap<>();
  static Map<Integer, Int> afterHistogram = new HashMap<>();

  public static void logMethod(final int statementsBefore, final int statementsAfter) {
    ++getSafe(beforeHistogram, Integer(statementsBefore)).inner;
    ++getSafe(afterHistogram, Integer(statementsAfter)).inner;
  }

  private static Int getSafe(final Map<Integer, Int> m, final Integer i) {
    m.putIfAbsent(i, new Int());
    return m.get(i);
  }

  private static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }
}
