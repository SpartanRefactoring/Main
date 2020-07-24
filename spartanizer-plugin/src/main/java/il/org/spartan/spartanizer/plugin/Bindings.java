package il.org.spartan.spartanizer.plugin;

import java.util.List;

import org.eclipse.jdt.core.dom.ITypeBinding;

/** Binding utilities.
 * @author Ori Roth
 * @since 2017-05-21
 * @see org.eclipse.jdt.internal.corext.dom.Bindings */
@SuppressWarnings("restriction")
public class Bindings {
  public static List<ITypeBinding> getAllFrom(final ITypeBinding b) {
    final List<ITypeBinding> $ = an.empty.list();
    if (b == null)
      return $;
    if (b.isArray())
      $.addAll(getAllFrom(b.getElementType()));
    else {
      if (b.isCapture()) {
        if (!b.isUpperbound())
          $.addAll(getAllFrom(b.getWildcard()));
        else
          for (final ITypeBinding ¢ : b.getTypeBounds())
            $.addAll(getAllFrom(¢));
        return $;
      }
      if (!b.isParameterizedType()) {
        $.add(b);
        return $;
      }
      $.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        $.addAll(getAllFrom(¢));
    }
    if (b.isCapture())
      if (!b.isUpperbound())
        $.addAll(getAllFrom(b.getWildcard()));
      else
        for (final ITypeBinding ¢ : b.getTypeBounds())
          $.addAll(getAllFrom(¢));
    else {
      if (!b.isParameterizedType()) {
        $.add(b);
        return $;
      }
      $.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        $.addAll(getAllFrom(¢));
    }
    if (!b.isParameterizedType())
      $.add(b);
    else {
      $.add(b.getTypeDeclaration());
      for (final ITypeBinding ¢ : b.getTypeArguments())
        $.addAll(getAllFrom(¢));
    }
    return $;
  }
}
