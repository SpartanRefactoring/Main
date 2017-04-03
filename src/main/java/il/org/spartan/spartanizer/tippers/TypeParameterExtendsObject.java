package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code T extends Object} to {@code T}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-16 */
public final class TypeParameterExtendsObject extends ReplaceCurrentNode<TypeParameter>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -0x7B57A434F5B0573CL;

  @Override public String description(final TypeParameter ¢) {
    return "Trim implicit extends " + trivia.gist(¢);
  }

  @Override public TypeParameter replacement(final TypeParameter p) {
    final TypeParameter $ = copy.of(p);
    final List<Type> ts = typeBounds($);
    if (!haz.hasObject(ts))
      return null;
    for (final Iterator<Type> ¢ = ts.iterator(); ¢.hasNext();)
      if (isObject(¢.next()))
        ¢.remove();
    return $;
  }
}
