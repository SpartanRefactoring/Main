package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.typeBounds;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Convert {@code T extends Object} to {@code T}
 * @author Yossi Gil
 * @since 2017-01-16 */
public final class TypeParameterExtendsObject extends ReplaceCurrentNode<TypeParameter>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x7B57A434F5B0573CL;

  @Override public String description(final TypeParameter ¢) {
    return "Trim implicit extends " + Trivia.gist(¢);
  }
  @Override public TypeParameter replacement(final TypeParameter p) {
    final TypeParameter $ = copy.of(p);
    final List<Type> ts = typeBounds($);
    if (!haz.hasObject(ts))
      return null;
    for (final Iterator<Type> ¢ = ts.iterator(); ¢.hasNext();)
      if (type.isObject(¢.next()))
        ¢.remove();
    return $;
  }
}
