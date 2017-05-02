package il.org.spartan.reflection;

import java.lang.reflect.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;

public class ClassPredicates {
  public static boolean hasMutableFields(@NotNull final Class<?> c) {
    for (@NotNull final Field ¢ : c.getDeclaredFields())
      if (!Modifier.isFinal(¢.getModifiers()))
        return true;
    final Class<?> $ = c.getSuperclass();
    return $ != null && hasMutableFields($);
  }

  public static boolean isImmutable(@NotNull final Class<?> ¢) {
    return !¢.isEnum() && nFields(¢) > 0 && !hasMutableFields(¢);
  }

  public static int nFields(@NotNull final Class<?> c) {
    int $ = 0;
    for (@NotNull final Field ¢ : c.getDeclaredFields())
      $ += as.bit(!¢.isSynthetic() && !Modifier.isStatic(¢.getModifiers()));
    final Class<?> parent = c.getSuperclass();
    return $ + (parent == null ? 0 : nFields(parent));
  }
}
