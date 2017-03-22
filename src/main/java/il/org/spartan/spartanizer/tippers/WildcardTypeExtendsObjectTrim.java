package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** Convert {@code ? extends Object} to {@code ?}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-16 */
public final class WildcardTypeExtendsObjectTrim extends ReplaceCurrentNode<WildcardType>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 6954094761117949540L;

  @NotNull
  @Override public String description(final WildcardType ¢) {
    return "Trim implicit extends " + trivia.gist(¢);
  }

  @Override public WildcardType replacement(@NotNull final WildcardType ¢) {
    if (!¢.isUpperBound() || !wizard.isObject(¢.getBound()))
      return null;
    final WildcardType $ = copy.of(¢);
    $.setBound(null);
    return $;
  }
}
