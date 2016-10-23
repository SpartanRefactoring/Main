package il.org.spartan.reflection;

import java.lang.reflect.*;

import org.jetbrains.annotations.*;

public class Reflection {
  @Nullable public static Class<?> typeOf(final Member ¢) {
    return ¢ instanceof Method ? ((Method) ¢).getReturnType() : ¢ instanceof Field ? ((Field) ¢).getType() : null;
  }
}
