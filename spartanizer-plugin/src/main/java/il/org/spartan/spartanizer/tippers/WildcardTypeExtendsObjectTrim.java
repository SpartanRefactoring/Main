package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.WildcardType;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Convert {@code ? extends Object} to {@code ?}
 * @author Yossi Gil
 * @since 2017-01-16 */
public final class WildcardTypeExtendsObjectTrim extends ReplaceCurrentNode<WildcardType>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x6081E85742D71E64L;

  @Override public String description(final WildcardType ¢) {
    return "Trim implicit extends " + Trivia.gist(¢);
  }
  @Override public WildcardType replacement(final WildcardType ¢) {
    if (!¢.isUpperBound() || !type.isObject(¢.getBound()))
      return null;
    final WildcardType $ = copy.of(¢);
    $.setBound(null);
    return $;
  }
}
