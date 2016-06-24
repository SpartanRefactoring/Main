package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.annotation.Nullable;

public class Utils {
  public static <@Nullable T> T unless(boolean condition, final T $) {
    return !condition ? $ : null;
  }
}
