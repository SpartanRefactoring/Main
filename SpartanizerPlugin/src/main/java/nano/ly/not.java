package nano.ly;

import il.org.spartan.spartanizer.ast.safety.*;

public interface not {
  @SafeVarargs static <T> boolean in(final T t, final T... ts) {
    return !iz.in(t, ts);
  }

  static boolean nil(final Object ¢) {
    return ¢ != null;
  }
}