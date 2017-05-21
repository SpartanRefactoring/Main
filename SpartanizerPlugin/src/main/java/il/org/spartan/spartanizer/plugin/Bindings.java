package il.org.spartan.spartanizer.plugin;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Binding utilities.
 * @author Ori Roth
 * @since 2017-05-21
 * @see org.eclipse.jdt.internal.corext.dom.Bindings */
@SuppressWarnings("restriction")
public class Bindings {
  public static List<ITypeBinding> getAllFrom(final ITypeBinding binding) {
    final List<ITypeBinding> $ = an.empty.list();
    if (binding == null)
      return $;
    if (binding.isArray()) {
      $.addAll(getAllFrom(binding.getElementType()));
      return $;
    }
    if (binding.isCapture()) {
      if (binding.isUpperbound())
        for (ITypeBinding b : binding.getTypeBounds())
          $.addAll(getAllFrom(b));
      else
        $.addAll(getAllFrom(binding.getWildcard()));
      return $;
    }
    if (!binding.isParameterizedType()) {
      $.add(binding);
      return $;
    }
    $.add(binding.getTypeDeclaration());
    for (ITypeBinding b : binding.getTypeArguments())
      $.addAll(getAllFrom(b));
    return $;
  }
}
