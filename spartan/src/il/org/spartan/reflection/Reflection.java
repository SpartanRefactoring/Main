package il.org.spartan.reflection;

import java.lang.reflect.*;

import org.eclipse.jdt.annotation.*;

public class Reflection {
   public static Class<?> typeOf(final Member ¢) {
    return ¢ instanceof Method ? ((Method) ¢).getReturnType() : ¢ instanceof Field ? ((Field) ¢).getType() : null;
  }
}
