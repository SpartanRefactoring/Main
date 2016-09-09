package il.org.spartan.reflection;

import java.lang.reflect.*;

public class Reflection {
  public static Class<?> typeOf(final Member m) {
    if (m instanceof Method)
      return ((Method) m).getReturnType();
    if (m instanceof Field)
      return ((Field) m).getType();
    return null;
  }
}
