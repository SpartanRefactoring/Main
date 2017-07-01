package il.org.spartan.spartanizer.plugin;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Binding utilities.
 * @author Ori Roth
 * @since 2017-05-21
 * @see org.eclipse.jdt.internal.corext.dom.Bindings */
@SuppressWarnings("restriction")
public class Bindings {
  public static List<ITypeBinding> getAllFrom(final ITypeBinding b) {
    final List<ITypeBinding> ret = an.empty.list();
    if (b == null)
      return ret;
    if (b.isArray())
      ret.addAll(getAllFrom(b.getElementType()));
    else {
      if (b.isCapture()) {
        if (!b.isUpperbound())
          ret.addAll(getAllFrom(b.getWildcard()));
        else
          for (final ITypeBinding ¢ : b.getTypeBounds())
            ret.addAll(getAllFrom(¢));
        return ret;
      }
      if (!b.isParameterizedType()) {
        ret.add(b);
        return ret;
      }
      ret.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        ret.addAll(getAllFrom(¢));
    }
    if (b.isCapture())
      if (!b.isUpperbound())
        ret.addAll(getAllFrom(b.getWildcard()));
      else
        for (final ITypeBinding ¢ : b.getTypeBounds())
          ret.addAll(getAllFrom(¢));
    else {
      if (!b.isParameterizedType()) {
        ret.add(b);
        return ret;
      }
      ret.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        ret.addAll(getAllFrom(¢));
    }
    if (!b.isParameterizedType())
      ret.add(b);
    else {
      ret.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        ret.addAll(getAllFrom(¢));
    }
    return ret;
  }
}
