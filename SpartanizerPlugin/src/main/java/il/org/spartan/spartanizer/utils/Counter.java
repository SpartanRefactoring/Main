package il.org.spartan.spartanizer.utils;

import java.util.*;

public class Counter {
  private static final Map<Class<?>, Integer> appearences = new HashMap<>();

  @SuppressWarnings("boxing") public static void count(final Class<?> ¢) {
    appearences.putIfAbsent(¢, 0);
    appearences.put(¢, Integer.valueOf(appearences.get(¢) + 1));
  }
}
