package il.org.spartan.reflection;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;

public class Reflection {
  @Nullable
  public static Class<?> typeOf(final Member ¢) {
    return ¢ instanceof Method ? ((Method) ¢).getReturnType() : ¢ instanceof Field ? ((Field) ¢).getType() : null;
  }
}
