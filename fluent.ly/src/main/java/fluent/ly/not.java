package fluent.ly;

import nano.ly.is;

public interface not {
  @SafeVarargs static <T> boolean in(final T t, final T... ts) {
    return !is.in(t, ts);
  }

  static boolean nil(final Object ¢) {
    return ¢ != null;
  }
}