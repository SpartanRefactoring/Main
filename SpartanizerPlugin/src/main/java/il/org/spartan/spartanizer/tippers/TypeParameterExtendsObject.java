package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code T extends Object} to {@code T}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-16 */
public final class TypeParameterExtendsObject extends ReplaceCurrentNode<TypeParameter>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -8887752937006192444L;

  @Override public String description(final TypeParameter ¢) {
    return "Trim implicit extends " + trivia.gist(¢);
  }

  @Override public TypeParameter replacement(final TypeParameter p) {
    final TypeParameter $ = copy.of(p);
    final List<Type> ts = typeBounds($);
    if (!hasObject(ts))
      return null;
    for (final Iterator<Type> ¢ = ts.iterator(); ¢.hasNext();)
      if (isObject(¢.next()))
        ¢.remove();
    return $;
  }
}
