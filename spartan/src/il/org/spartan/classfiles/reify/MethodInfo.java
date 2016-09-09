/**
 *
 */
package il.org.spartan.classfiles.reify;

import java.lang.reflect.*;

import il.org.spartan.classfiles.reify.TypeInfo.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class MethodInfo extends ExecutableEntity {
  private static String class2name(final Class<?> param) {
    return param.toString().startsWith("class ") ? param.toString().substring(param.toString().indexOf(' ') + 1) : param.toString();
  }

  private static String[] class2name(final Class<?>[] params) {
    final String[] strParams = new String[params.length];
    for (int i = 0; i < params.length; i++)
      strParams[i] = class2name(params[i]);
    return strParams;
  }

  private static String signature(final String name, final String returnType, final String[] parameterTypes) {
    return name + ":" + returnType + " (" + Separate.by(parameterTypes, ", ") + ")";
  }

  public MethodInfo(final TypedEntity t) {
    super(t.constantPool, t.flags, t.name, t.descriptor, t.attributes);
  }

  @Attribute public int argumentsCount() {
    return decodeArguments(Iterables.make(descriptor.substring(1) // skip (
        .toCharArray()).iterator()).length;
  }

  public MethodType getMethodType() {
    return (MethodType) type;
  }

  public boolean isObjectMethod() {
    try {
      final Class<?> objectClass = Class.forName("java.lang.Object");
      for (final Method m : objectClass.getMethods()) {
        if (!m.getName().equals(name))
          continue;
        final Class<?>[] params = m.getParameterTypes();
        final String[] strParams = class2name(params);
        final String s = signature(m.getName(), class2name(m.getReturnType()), strParams);
        if (s.equals(signature()))
          return true;
      }
    } catch (final ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  public int responseForMethod() {
    return getReferencedMethods().size();
  }
}
