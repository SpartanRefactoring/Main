package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** 
 * Convert <code>? extends Object</code> to <code>?</code>
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-16
 */
public final class WildcardTypeExtendsObjectTrim extends ReplaceCurrentNode<WildcardType> implements TipperCategory.Collapse {
  @Override public String description(final WildcardType ¢) {
    return "Trim implicit extends " + wizard.trim(¢);
  }

  @Override public WildcardType replacement(WildcardType ¢) {
    if (!¢.isUpperBound() || !wizard.isObject(¢.getBound()))
      return null;
    final WildcardType $ = copy.of(¢);
    $.setBound(null);
    return $;
  }
}
