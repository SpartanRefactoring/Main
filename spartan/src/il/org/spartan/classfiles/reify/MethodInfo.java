package il.org.spartan.classfiles.reify;

import java.lang.reflect.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.classfiles.reify.TypeInfo.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class MethodInfo extends ExecutableEntity {
   private static String class2name(final Class<?> param) {
    return !(param + "").startsWith("class ") ? param + "" : (param + "").substring((param + "").indexOf(' ') + 1);
  }
   private static String[] class2name( final Class<?>[] params) {
     final String[] $ = new String[params.length];
    for (int ¢ = 0; ¢ < params.length; ++¢)
      $[¢] = class2name(params[¢]);
    return $;
  }
   private static String signature(final String name, final String returnType,  final String[] parameterTypes) {
    return name + ":" + returnType + " (" + Separate.by(parameterTypes, ", ") + ")";
  }
  public MethodInfo( final TypedEntity t) {
    super(t.constantPool, t.flags, t.name, t.descriptor, t.attributes);
  }
  @Attribute public int argumentsCount() {
    return decodeArguments(Iterables.make(descriptor.substring(1) // skip (
        .toCharArray()).iterator()).length;
  }
   public MethodType getMethodType() {
    return (MethodType) type;
  }
  public boolean notObjectMethod() {
    try {
      for ( final Method ¢ : Class.forName("java.lang.Object").getMethods())
        if (¢.getName().equals(name) && signature(¢.getName(), class2name(¢.getReturnType()), class2name(¢.getParameterTypes())).equals(signature()))
          return false;
    } catch ( final ClassNotFoundException ¢) {
      ¢.printStackTrace();
    }
    return true;
  }
  public int responseForMethod() {
    return getReferencedMethods().size();
  }
}
