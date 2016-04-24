package il.org.spartan.refactoring.utils;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.corext.dom.Bindings;

/**
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24
 */
@SuppressWarnings("restriction")
public class BindingUtils {
  public static IMethodBinding getPublicMethod(ITypeBinding t, String n, ITypeBinding[] ps) {
    IMethodBinding $ = Bindings.findMethodInHierarchy(t, n, ps);
    if (!Modifier.isPublic($.getModifiers()))
      return null;
    return $;
  }
}
