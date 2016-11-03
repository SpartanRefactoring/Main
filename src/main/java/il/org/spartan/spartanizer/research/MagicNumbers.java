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
    ++safeGet(beforeHistogram, Integer(statementsBefore)).inner;
    ++safeGet(afterHistogram, Integer(statementsAfter)).inner;
  }

  private static Int safeGet(final Map<Integer, Int> m, final Integer i) {
    if (!m.containsKey(i))
      m.put(i, new Int());
    return m.get(m);
  }

  private static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }
}
