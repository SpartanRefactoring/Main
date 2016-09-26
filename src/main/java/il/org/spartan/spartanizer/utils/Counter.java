package il.org.spartan.spartanizer.utils;

import java.util.*;

public class Counter {
  final static private Map<Class<?>, Integer> appearences = new HashMap<>();

  @SuppressWarnings("boxing") public static void count(final Class<?> ¢) {
    if (!appearences.containsKey(¢))
      appearences.put(¢, 0);
    appearences.put(¢, new Integer(appearences.get(¢) + 1));
  }
}
