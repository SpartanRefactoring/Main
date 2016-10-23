package il.org.spartan.reflection;

import java.lang.reflect.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

public class ClassPredicates {
  public static boolean hasMutableFields(@NotNull final Class<?> c) {
    for (final Field ¢ : c.getDeclaredFields())
      if (!Modifier.isFinal(¢.getModifiers()))
        return true;
    final Class<?> parent = c.getSuperclass();
    return parent != null && hasMutableFields(parent);
  }

  public static boolean isImmutable(@NotNull final Class<?> ¢) {
    return !¢.isEnum() && nFields(¢) > 0 && !hasMutableFields(¢);
  }

  public static int nFields(@NotNull final Class<?> c) {
    int $ = 0;
    for (final Field ¢ : c.getDeclaredFields())
      $ += as.bit(!¢.isSynthetic() && !Modifier.isStatic(¢.getModifiers()));
    final Class<?> parent = c.getSuperclass();
    return $ + (parent == null ? 0 : nFields(parent));
  }
}
