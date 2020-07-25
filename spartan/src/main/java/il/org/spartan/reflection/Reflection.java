package il.org.spartan.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class Reflection {
  public static Class<?> typeOf(final Member ¢) {
    return ¢ instanceof Method ? ((Method) ¢).getReturnType() : ¢ instanceof Field ? ((Field) ¢).getType() : null;
  }
}
