package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.annotation.*;

public class Utils {
  public static <@Nullable T> T unless(final boolean condition, final T $) {
    return !condition ? $ : null;
  }
}
