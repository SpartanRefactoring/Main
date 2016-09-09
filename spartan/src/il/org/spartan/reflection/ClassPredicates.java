package il.org.spartan.reflection;

import java.lang.reflect.*;

import il.org.spartan.utils.*;

public class ClassPredicates {
  public static boolean hasMutableFields(final Class<?> c) {
    for (final Field f : c.getDeclaredFields())
      if (!Modifier.isFinal(f.getModifiers()))
        return true;
    final Class<?> parent = c.getSuperclass();
    return parent != null && hasMutableFields(parent);
  }

  public static boolean isImmutable(final Class<?> c) {
    return !c.isEnum() && nFields(c) > 0 && !hasMutableFields(c);
  }

  public static int nFields(final Class<?> c) {
    int $ = 0;
    for (final Field f : c.getDeclaredFields())
      $ += As.binary(!f.isSynthetic() && !Modifier.isStatic(f.getModifiers()));
    final Class<?> parent = c.getSuperclass();
    return $ + (parent == null ? 0 : nFields(parent));
  }
}
