package il.org.spartan.utils;

import il.org.spartan.spartanizer.ast.safety.*;

public interface not {
  @SafeVarargs static <T> boolean in(final T t, final T... ts) {
    return !iz.in(t, ts);
  }

  static boolean null¢(final Object ¢) {
    return ¢ != null;
  }
}