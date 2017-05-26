package il.org.spartan.reflection;

import java.lang.reflect.*;

public class Reflection {
  public static Class<?> typeOf(final Member ¢) {
    return ¢ instanceof Method ? ((Method) ¢).getReturnType() : ¢ instanceof Field ? ((Field) ¢).getType() : null;
  }
}
