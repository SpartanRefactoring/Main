package il.org.spartan.spartanizer.utils;

import java.util.*;

public class Counter {
  final static private Map<Class<?>, Integer> appearences = new HashMap<>();

  @SuppressWarnings("boxing") public static void count(final Class<?> c) {
    if (!appearences.containsKey(c))
      appearences.put(c, 0);
    appearences.put(c, new Integer(appearences.get(c) + 1));
  }
}
