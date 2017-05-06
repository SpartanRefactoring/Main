package il.org.spartan.reflection;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.annotation.*;

/** This class computes the deep size of any object. */
public class DeepSize {
  public static int of( final boolean it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final byte it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final char it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final double it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final float it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final int it[]) {
    return ShallowSize.of(it);
  }
  public static int of( final long it[]) {
    return ShallowSize.of(it);
  }
  public static int of(final Object ¢) {
    return new Visitor().size(¢);
  }
  public static int of( final short it[]) {
    return ShallowSize.of(it);
  }

  static class Visitor {
     private static Class<?>[] nonReference = new Class<?>[] { //
        boolean.class, char.class, void.class, //
        boolean[].class, char[].class, //
        byte.class, short.class, int.class, long.class, //
        byte[].class, short[].class, int[].class, long[].class, //
        float.class, double.class, //
        float[].class, double[].class,//
    };

     static ArrayList<Field> getAllFields(final Class<?> c) {
       final ArrayList<Field> $ = new ArrayList<>();
      for (Class<?> p = c; p != null; p = p.getSuperclass())
        for (final Field ¢ : p.getDeclaredFields())
          $.add(¢);
      return $;
    }
    private static Object get( final Field $, final Object o) {
      $.setAccessible(true);
      try {
        return $.get(o);
      } catch ( final IllegalAccessException | IllegalArgumentException ¢) {
        throw new RuntimeException(¢);
      }
    }
    private static boolean isReference( final Field f) {
      final Class<?> c = f.getType();
      for (final Class<?> p : nonReference)
        if (p == c)
          return false;
      return true;
    }

    final Set<Object> seen = new HashSet<>();

    public int size( final Object ¢) {
      if (seen.contains(¢))
        return 0;
      seen.add(¢);
      return ¢ == null ? 0 : size(¢, ¢.getClass());
    }
    int size(final Object o,  final Class<?> c) {
      if (c.isArray())
        return size(Object[].class.cast(o));
      int $ = ShallowSize.of(o);
      for ( final Field ¢ : getAllFields(c))
        $ += size(o, ¢);
      // System.out.println("$ is:" + $);
      return $;
    }
    private int size(final Object o,  final Field f) {
      return Modifier.isStatic(f.getModifiers()) || !isReference(f) ? 0 : size(get(f, o));
    }
    private int size( final Object[] os) {
      int $ = ShallowSize.of(os);
      for (final Object ¢ : os)
        $ += size(¢);
      return $;
    }
  }
}
